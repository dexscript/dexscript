# matched

```dexscript
case <-example {}
```

* consumeStmt
    * "<-example"

# missing_left_brace

```dexscript
case <-example }
```

```dexscript
<unmatched>case <-example }</unmatched>
```

# missing_right_brace

```dexscript
case <-example {
```

* blk
    * `{<error/>`




