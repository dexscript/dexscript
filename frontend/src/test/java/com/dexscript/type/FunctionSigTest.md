# without_type_params

```dexscript
(arg0: string): int64
```

| invocation | success | needRuntimeCheck |
| ---        | ---     | ---              |
| `'abc'`    | true    | false            |
| `string`   | true    | false            |
| `int64`    | false   | false            |

