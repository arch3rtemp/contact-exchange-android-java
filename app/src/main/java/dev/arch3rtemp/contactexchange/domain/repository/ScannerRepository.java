package dev.arch3rtemp.contactexchange.domain.repository;

import dev.arch3rtemp.contactexchange.domain.model.Card;
import io.reactivex.rxjava3.core.Single;

public interface ScannerRepository {
    Single<Card> scan();
}
