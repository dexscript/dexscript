# matched

```dexscript
case AA() {}
```

* identifier
    * "AA"

# missing_identifier

```dexscript
case ??() {}
```

```dexscript
<unmatched>case ??() {}</unmatched>
```

# missing_blank

```dexscript
caseAA() {}
```

```dexscript
<unmatched>caseAA() {}</unmatched>
```

# missing_left_paren

```dexscript
case AA) {}
```

```dexscript
<unmatched>case AA) {}</unmatched>
```

# missing_right_paren

```dexscript
case AA( {}
```

```dexscript
case AA( {}<error/>
```

# missing_block

```dexscript
case AA() {
```

```dexscript
case AA() {
```

* blk
    * `{<error/>`










