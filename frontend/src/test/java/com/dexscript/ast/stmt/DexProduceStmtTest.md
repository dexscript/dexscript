# matched

```dexscript
resolve 'hello' -> example
```

* produced
    * `'hello'`
* target
    * `example`

# produced_is_optional

```dexscript
resolve-> example
```

* produced
    * null
* target
    * `example`

# no_space_after_produce_keyword

```dexscript
resolve'hello' -> example
```

```dexscript
<unmatched>resolve'hello' -> example</unmatched>
```

# missing_produced_recover_by_blank

```dexscript
resolve ?? -> example
```

```dexscript
resolve <error/>?? -> example
```

# missing_produced_recover_by_line_end

```dexscript
resolve ; -> example
```

```dexscript
resolve <error/>
```

# missing_right_arrow_recover_by_blank

```dexscript
resolve 'hello' ? example
```

```dexscript
resolve 'hello' <error/>? example
```

# missing_right_arrow_recover_by_line_end

```dexscript
resolve 'hello' ?; example
```

```dexscript
resolve 'hello' <error/>?
```

# missing_target

```dexscript
resolve 'hello' -> ??;example
```

```dexscript
resolve 'hello' -><error/> ??
```











