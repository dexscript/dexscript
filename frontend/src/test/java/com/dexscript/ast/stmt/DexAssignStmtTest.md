# one_target

```dexscript
a=b
```

* targets
    * size
        * 1
    * [0]
        * "a"
* expr
    * "b"

# two_targets

```dexscript
a,b=c
```

* targets
    * size
        * 2
    * [0]
        * "a"
    * [1]
        * "b"
* expr
    * "c"

# missing_expr

```dexscript
a=??
```

```dexscript
<unmatched>a=??</unmatched>
```




