package dev.arch3rtemp.contactexchange.domain.usecase;

import javax.inject.Inject;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import dev.arch3rtemp.contactexchange.domain.repository.ScannerRepository;
import io.reactivex.rxjava3.core.Single;

public final class ScanQrUseCase {

    private final ScannerRepository scanner;
    @Inject
    public ScanQrUseCase(ScannerRepository scanner) {
        this.scanner = scanner;
    }

    public Single<Card> invoke() {
        return scanner.scan();
    }
}
