# unary_then_binary

```dexscript
+a+b
```

* left
    * "+a"
* right
    * "b"

# binary_then_unary

```dexscript
a+-b
```

* left
    * "a"
* right
    * "-b"

# add_has_lower_rank_than_mul_1

```dexscript
a+b*c
```

* left
    * "a"
* right
    * "b*c"
    * left
        * "b"
    * right
        * "c"

# add_has_lower_rank_than_mul_2

```dexscript
a*b+c
```

* left
    * "a*b"
    * left
        * "a"
    * right
        * "b"
* right
    * "c"