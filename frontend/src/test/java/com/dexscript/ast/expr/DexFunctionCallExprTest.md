# zero_argument

```dexscript
print()
```

* target
    * "print"
* typeArgs
    * size
        * 0
* posArgs
    * size
        * 0
* namedArgs
    * size
        * 0

# one_pos_arg

```dexscript
print(a)
```

* target
    * "print"
* typeArgs
    * size
        * 0
* posArgs
    * size
        * 1
    * [0]
        * "a"
* namedArgs
    * size
        * 0

# three_pos_args

```dexscript
print(a1, b1, c1)
```

* posArgs
    * size
        * 3
    * [0]
        * "a1"
    * [1]
        * "b1"
    * [2]
        * "c1"

# pos_arg_with_extra_comma

```dexscript
print(a,)
```

* posArgs
    * size
        * 1
    * [0]
        * "a"

# two_call_separated_by_new_line

```dexscript
hello()
world()
```

```dexscript
hello()
```

# one_type_arg

```dexscript
Hello<uint8>()
```

* target
    * "Hello"
* typeArgs
    * size
        * 1
    * [0]
        * "uint8"

# named_arg_after_pos_arg

```dexscript
print(a1,b1,c1,d=d1)
```

* posArgs
    * size
        * 3
* namedArgs
    * size
        * 1
    * [0]
        * name
            * "d"
        * val
            * "d1"

# two_named_args

```dexscript
print(a1,b1,c1,d=d1,e=e1)
```

* posArgs
    * size
        * 3
* namedArgs
    * size
        * 2
    * [0]
        * name
            * "d"
        * val
            * "d1"
    * [1]
        * name
            * "e"
        * val
            * "e1"

# leading_named_arg

```dexscript
print(d=d1)
```

* posArgs
    * size
        * 0
* namedArgs
    * size
        * 1
    * [0]
        * name
            * "d"
        * val
            * "d1"

# pos_arg_after_named_arg

```dexscript
print(d=d1,c1)
```

```dexscript
print(d=d1,<error/>c1)
```

# context_arg

```dexscript
print($=$)
```

* posArgs
    * size
        * 0
* namedArgs
    * size
        * 1
    * [0]
        * name
            * "$"
        * val
            * "$"

# invalid_pos_arg_recover_by_right_paren

```dexscript
print(?)a
```

```dexscript
print(<error/>?)
```

# invalid_argument_recover_by_comma

```dexscript
print(?,)a
```

```dexscript
print(<error/>?,)
```

# invalid_argument_recover_by_line_end

```dexscript
print(?
a
```

```dexscript
print(<error/>?
```

# invalid_argument_recover_by_file_end

```dexscript
print(?
```

```dexscript
print(<error/>?
```


# argument_followed_by_invalid_argument

```dexscript
print(?,a)
```

```dexscript
print(<error/>?,a)
```

# missing_right_paren_recover_by_file_end

```dexscript
print(a
```

```dexscript
print(a<error/>
```

# missing_right_paren_recover_by_line_end

```dexscript
print(a;b
```

```dexscript
print(a<error/>
```

# missing_named_arg_val

```dexscript
print(d=)
```

```dexscript
print(d=<error/>)
```
