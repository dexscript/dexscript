# no_const

```dexscript
string
```

```dexscript
a=int64
```

| posArgs | namedArgs |
| ---     | ---     |
| `string`  | `a=int64` |

# one_bool_const

```dexscript
(const)true, string
```

| posArgs |
| ---     |
| `true, string`  |
| `bool, string`  |

# one_string_const

```dexscript
(const)'hello', string
```

| posArgs |
| ---     |
| `'hello', string`  |
| `string, string`  |

# one_integer_const

```dexscript
(const)100, string
```

| posArgs |
| ---     |
| `100, string`  |
| `int64, string`  |
| `int32, string`  |
| `float64, string`  |
| `float32, string`  |

# one_float_const

```dexscript
(const)100.0, string
```

| posArgs |
| ---     |
| `float64, string`  |
| `float32, string`  |

# one_pos_const_and_one_named_const

```dexscript
(const)true
```

```dexscript
a=(const)false
```

| posArgs | namedArgs |
| ---     | ---     |
| `true`  | `a=false` |
| `bool`  | `a=false` |
| `true`  | `a=bool` |
| `bool`  | `a=bool` |

# two_bool_const

```dexscript
(const)true, (const)false
```

| posArgs |
| ---     |
| `true, false`  |
| `bool, false`  |
| `true, bool`  |
| `bool, bool`  |

# three_bool_const

```dexscript
(const)true, (const)false, (const)true
```

| posArgs |
| ---     |
| `true, false, true`  |
| `bool, false, true`  |
| `true, bool, true`  |
| `bool, bool, true`  |
| `true, false, bool`  |
| `bool, false, bool`  |
| `true, bool, bool`  |
| `bool, bool, bool`  |






