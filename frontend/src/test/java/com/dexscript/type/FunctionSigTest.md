# pos_param_assignable_from_arg

```dexscript
(arg0: string): int64
```

| posArgs    | success | needRuntimeCheck |
| ---        | ---     | ---              |
| `'abc'`    | true    | false            |
| `string`   | true    | false            |
| `int64`    | false   | false            |

# pos_arg_assignable_from_param

```dexscript
(arg0: 'abc'): int64
```

| posArgs    | success | needRuntimeCheck |
| ---        | ---     | ---              |
| `'abc'1`   | true    | false            |
| `string`   | true    | true             |
| `int64`    | false   | false            |

# pos_args_count

```dexscript
(arg0: string, arg1: string): int64
```

| posArgs          | success |
| ---              | ---     |
| `string`         | false   |
| `string, string` | true    |

# type_args_count

```dexscript
(<T>: string): int64
```

| typeArgs | posArgs | success |
| ---      | ---     | ---     |
| `string` |         | true    |
|          |         | false   |

# infer_type_params

```dexscript
(<T>: string, arg0: T): T
```

| posArgs  | func.ret | success |
| ---      | ---      | ---     |
| `string` | "string" | true    |
| `'abc'`  | "'abc'"  | true    |
| `int64`  | "T"      | false   |
