package com.dexscript.observability.stat;

import com.dexscript.observability.Transaction;

import java.util.function.Consumer;

public class Counter implements Consumer<Transaction> {
    @Override
    public void accept(Transaction tx) {
    }
}
