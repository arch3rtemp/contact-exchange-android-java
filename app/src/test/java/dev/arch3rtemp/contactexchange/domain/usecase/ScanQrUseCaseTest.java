package dev.arch3rtemp.contactexchange.domain.usecase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.core.os.OperationCanceledException;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import dev.arch3rtemp.contactexchange.RxTrampolineRule;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.domain.repository.ScannerRepository;
import io.reactivex.rxjava3.core.Single;

public class ScanQrUseCaseTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    public ScannerRepository repository;

    @InjectMocks
    private ScanQrUseCase scanQr;

    @Test
    public void invoke_emitsValidData() {
        when(repository.scan()).thenReturn(Single.just(TestData.testScannedCard));

        scanQr.invoke()
                .test()
                .assertComplete()
                .assertValue(TestData.testScannedCard);

        verify(repository).scan();
    }

    @Test
    public void invoke_emitsCancellationError() {
        when(repository.scan()).thenReturn(Single.error(new OperationCanceledException()));

        scanQr.invoke()
                .test()
                .assertError(OperationCanceledException.class)
                .assertNoValues();

        verify(repository).scan();
    }

    @Test
    public void invoke_emitsError() {
        when(repository.scan()).thenReturn(Single.error(new Exception()));

        scanQr.invoke()
                .test()
                .assertError(Exception.class)
                .assertNoValues();

        verify(repository).scan();
    }
}
