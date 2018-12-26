# matched

```dexscript
<T>: string
```

# missing_identifier_recover_by_right_angle_bracket

```dexscript
<>: string
```

```dexscript
<<error/>>: string
```

# missing_identifier_recover_by_blank

```dexscript
< >: string
```

```dexscript
<<error/> >: string
```

# missing_identifier_recover_by_line_end

```dexscript
<;>: string
```

```dexscript
<<error/>
```

# missing_right_angle_bracket_recover_by_colon

```dexscript
<T: string
```

```dexscript
<T<error/>: string
```

# missing_right_angle_bracket_recover_by_blank

```dexscript
<T : string
```

```dexscript
<T <error/>: string
```

# missing_colon_recover_by_blank

```dexscript
<T> string
```

```dexscript
<T> <error/>string
```

# missing_type

```dexscript
<T>:??;abc
```

```dexscript
<T>:<error/>??
```




