# zero_argument

```dexscript
new world()
```

* target
    * "world"
* typeArgs
    * size
        * 0
* posArgs
    * size
        * 0
* namedArgs
    * size
        * 0

# three_pos_args

```dexscript
new print(a1,b1,c1)
```

* target
    * "print"
* typeArgs
    * size
        * 0
* posArgs
    * size
        * 3
    * [0]
        * "a1"
    * [1]
        * "b1"
    * [2]
        * "c1"
* namedArgs
    * size
        * 0

# missing_target

```dexscript
new ?()
```

```dexscript
new <error/>?()
```

# missing_function_call

```dexscript
new print a1,b1,c1)
```

```dexscript
new print<error/> a1,b1,c1)
```

# prefix_with_double_colon

```dexscript
new ::abc()
```

* target
    * isGlobalScope
        * true

