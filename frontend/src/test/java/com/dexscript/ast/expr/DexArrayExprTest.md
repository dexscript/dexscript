# empty

```dexscript
[]
```

* elems
    * size
        * 0

# one_element

```dexscript
[1]
```

* elems
    * size
        * 1
    * [0]
        * "1"

# two_elements

```dexscript
[1, 2]
```

* elems
    * size
        * 2
    * [0]
        * "1"
    * [1]
        * "2"

# missing_element

```dexscript
[??, 2]
```

```dexscript
[<error/>??, 2]
```

# missing_right_bracket

```dexscript
[1, 2
```

```dexscript
[1, 2<error/>
```


