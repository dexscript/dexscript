# matched

```dexscript
(a)
```

* body
    * "a"

# empty_paren

```dexscript
()
```

```dexscript
(<error/>)
```


* body
    * `<error/>`

# nested_empty_paren

```dexscript
(())
```

* body
    * `(<error/>)`

# missing_right_paren_after_body

```dexscript
((a+b)*c?;b
```

```dexscript
((a+b)*c?<error/>
```

# missing_right_paren_without_body

```dexscript
(???
b
```

```dexscript
(<error/>???
```




