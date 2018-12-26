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

# mul_and_div_has_equal_rank_1

```dexscript
a*b/c+d
```

* left
    * "a*b/c"
    * left
        * "a*b"
    * right
        * "c"
* right
    * "d"

# mul_and_div_has_equal_rank_2

```dexscript
a/b*c+d
```

* left
    * "a/b*c"
    * left
        * "a/b"
    * right
        * "c"
* right
    * "d"

# paren_override_rank

```dexscript
(a+b)*c
```

* left
    * "(a+b)"
* right
    * "c"

# float_literal_is_preferred_over_integer_literal_1

```dexscript
1.34e100
```

* getClass
    * getSimpleName
        * "DexFloatConst"

# float_literal_is_preferred_over_integer_literal_2

```dexscript
100
```

* getClass
    * getSimpleName
        * "DexIntegerConst"

# mix_function_call_and_method_call

```dexscript
a().b()
```

* obj
    * "a()"
* method
    * "b"
