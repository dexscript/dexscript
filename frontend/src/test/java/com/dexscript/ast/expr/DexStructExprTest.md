# one_field

```dexscript
{a:b}
```

* fields
    * size
        * 1
    * [0]
        * name
            * "a"
        * val
            * "b"

# two_fields

```dexscript
{
    a: b,
    c: d
}
```

* fields
    * size
        * 2
    * [0]
        * name
            * "a"
        * val
            * "b"
    * [1]
        * name
            * "c"
        * val
            * "d"

# zero_field_is_invalid

```dexscript
{}
```

```dexscript
<unmatched>{}</unmatched>
```

# field_name_should_not_be_quoted

```dexscript
{'a': b}
```

```dexscript
<unmatched>{'a': b}</unmatched>
```

# missing_field_value

```dexscript
{a:}
```

```dexscript
{a:<error/>}
```

# missing_field_name

```dexscript
{a:b,:d}
```

```dexscript
{a:b,<error/>:d}
```

# missing_colon

```dexscript
{a:b,c d}
```

```dexscript
{a:b,c <error/>d}
```

# missing_right_brace

```dexscript
{a:b,c:d
```

```dexscript
{a:b,c:d<error/>
```









