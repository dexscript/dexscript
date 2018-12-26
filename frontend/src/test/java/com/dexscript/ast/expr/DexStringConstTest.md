# matched

```dexscript
'hello'
```

* constValue
    * `hello`

# escape_single_quote

```dexscript
'hello\'world'
```

* constValue
    * `hello'world`

# backslash_without_following_char

```dexscript
'hello\
```

```dexscript
'hello\<error/>
```

# missing_right_quote

```dexscript
'hello
```

```dexscript
'hello<error/>
```

# without_left_quote

```dexscript
hello'
```

```dexscript
<unmatched>hello'</unmatched>
```



