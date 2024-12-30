package dev.arch3rtemp.contactexchange.domain.usecase;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.data.scanner.QrScanner;
import dev.arch3rtemp.contactexchange.domain.model.Card;
import io.reactivex.rxjava3.core.Single;

public class ScanQrUseCase {

    private final QrScanner scanner;
    @Inject
    public ScanQrUseCase(QrScanner scanner) {
        this.scanner = scanner;
    }

    public Single<Card> invoke() {
        return scanner.scan();
    }
}
