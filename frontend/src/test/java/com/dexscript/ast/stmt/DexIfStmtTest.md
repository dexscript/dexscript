# no_else

```dexscript
if a==b {}
```

* condition
    * "a==b"

# with_else_if_only

```dexscript
if a==b {
} else if a==c {
}
```

* condition
    * "a==b"
* elseStmt
    * ifStmt
        * condition
            * "a==c"

# with_else_only

```dexscript
if a==b {
} else {
}
```

* condition
    * "a==b"
* elseStmt
    * ifStmt
        * matched
            * false

# with_else_if_and_else

```dexscript
if a==b {
} else if a==c {
} else {}
```

* condition
    * "a==b"
* elseStmt
    * ifStmt
        * condition
            * "a==c"
        * elseStmt
            * blk
                * "{}"

# else_before_else_if

```dexscript
if a==b {} else {} else if a==c {}
```

```dexscript
if a==b {} else {}
```
