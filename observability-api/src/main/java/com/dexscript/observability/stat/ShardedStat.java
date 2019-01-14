package com.dexscript.observability.stat;

import com.dexscript.observability.Transaction;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShardedStat implements Consumer<Transaction> {

    private final Supplier<Consumer<Transaction>> supplier;
    private final int shardsCount;

    public ShardedStat(Supplier<Consumer<Transaction>> supplier, int shardsCount) {
        this.supplier = supplier;
        this.shardsCount = shardsCount;
    }

    @Override
    public void accept(Transaction tx) {
    }
}
