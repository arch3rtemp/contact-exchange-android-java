package dev.arch3rtemp.contactexchange.data.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.core.os.OperationCanceledException;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import dev.arch3rtemp.contactexchange.R;
import dev.arch3rtemp.contactexchange.TestData;
import dev.arch3rtemp.contactexchange.data.mapper.JsonToCardMapper;
import dev.arch3rtemp.contactexchange.rx.RxTrampolineRule;
import dev.arch3rtemp.ui.util.StringResourceManager;

public class ScannerRepositoryImplTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public RxTrampolineRule rxTrampolineRule = new RxTrampolineRule();

    @Mock
    public JsonToCardMapper mockMapper;

    @Mock
    public StringResourceManager mockResourceManager;

    @Mock
    public GmsBarcodeScanner mockScanner;

    @Mock
    public Task<Barcode> mockBarcodeTask;

    @Mock
    public Barcode mockBarcode;

    @InjectMocks
    private ScannerRepositoryImpl repository;

    @Before
    public void setUp() {
        when(mockScanner.startScan()).thenReturn(mockBarcodeTask);
    }

    @Test
    public void invokeScan_returnsValidCard() throws JSONException {
        when(mockBarcode.getRawValue()).thenReturn(TestData.testCardJson);
        when(mockMapper.fromJson(TestData.testCardJson)).thenReturn(TestData.testScannedCard);
        when(mockBarcodeTask.addOnSuccessListener(any())).thenAnswer(new Answer<Task<Barcode>>() {
            @Override
            public Task<Barcode> answer(InvocationOnMock invocation) throws Throwable {
                // Capture the OnSuccessListener<Barcode> passed to addOnSuccessListener(...)
                OnSuccessListener<Barcode> listener = invocation.getArgument(0);

                // Invoke onSuccess(...) to simulate success
                listener.onSuccess(mockBarcode);

                // Return the mock task
                return mockBarcodeTask;
            }
        });
        when(mockBarcodeTask.addOnCanceledListener(any())).thenReturn(mockBarcodeTask);
        when(mockBarcodeTask.addOnFailureListener(any())).thenReturn(mockBarcodeTask);

        repository.scan()
                .test()
                .assertValue(TestData.testScannedCard);

        verify(mockScanner).startScan();
        verify(mockBarcode).getRawValue();
        verify(mockMapper).fromJson(TestData.testCardJson);
    }

    @Test
    public void invokeScan_throwsOperationCanceledExceptionOnCancel() {
        when(mockResourceManager.string(R.string.msg_scan_cancelled)).thenReturn("Scan cancelled");
        when(mockBarcodeTask.addOnSuccessListener(any())).thenReturn(mockBarcodeTask);
        when(mockBarcodeTask.addOnCanceledListener(any())).thenAnswer(new Answer<>() {
            @Override
            public Task<Barcode> answer(InvocationOnMock invocation) throws Throwable {
                OnCanceledListener listener = invocation.getArgument(0);
                listener.onCanceled();
                return mockBarcodeTask;
            }
        });
        when(mockBarcodeTask.addOnFailureListener(any())).thenReturn(mockBarcodeTask);

        repository.scan()
                .test()
                .assertFailure(OperationCanceledException.class);
    }
    
    @Test
    public void invokeScan_throwsRuntimeExceptionOnFailure() {
        when(mockBarcodeTask.addOnSuccessListener(any())).thenReturn(mockBarcodeTask);
        when(mockBarcodeTask.addOnCanceledListener(any())).thenReturn(mockBarcodeTask);
        when(mockBarcodeTask.addOnFailureListener(any())).thenAnswer(new Answer<>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                OnFailureListener listener = invocation.getArgument(0);
                listener.onFailure(new RuntimeException("Operation failure"));
                return mockBarcodeTask;
            }
        });

        repository.scan()
                .test()
                .assertFailure(RuntimeException.class);
    }
}
