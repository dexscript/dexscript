# one_target

```dexscript
a:=b
```

* decls
    * size
        * 1
    * [0]
        * "a"
* expr
    * "b"

# two_targets

```dexscript
a,b:=c
```

* decls
    * size
        * 2
    * [0]
        * "a"
    * [1]
        * "b"
* expr
    * "c"

# invalid_identifier

```dexscript
?:=b
```

```dexscript
<unmatched>?:=b</unmatched>
```

# second_decl_invalid

```dexscript
a,?:=b
```

```dexscript
<unmatched>a,?:=b</unmatched>
```

# missing_comma

```dexscript
a(b:=c
```

```dexscript
<unmatched>a(b:=c</unmatched>
```

# missing_colon

```dexscript
a,b(=c
```

```dexscript
<unmatched>a,b(=c</unmatched>
```

# expr_with_error

```dexscript
a:=
```

```dexscript
a:=<error/>
```








