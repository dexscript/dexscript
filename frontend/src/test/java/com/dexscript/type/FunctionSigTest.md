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
| `'abc'`    | true    | false            |
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

# infer_one_direct_placeholder

```dexscript
(<T>: string, arg0: T): T
```

| posArgs  | func.ret | `func.params[0].type` | success |
| -------- | -------- | ------------------- | ------- |
| `string` | `string` | `string`            | true    |
| `'abc'`  | `'abc'`  | `'abc'`             | true    |
| `int64`  | `T`      | `T`                 | false   |

# infer_one_parameterized_type

```dexscript
interface SomeInf {
    <T>: interface{}
    Get__(arg: T)
}
```

```dexscript
(<T>: string, arg0: SomeInf<T>): T
```

| posArgs           | func.ret | success |
| ----------------- | -------- | ------- |
| `SomeInf<string>` | `string` | true    |
| `SomeInf<'abc'>`  | `'abc'`  | true    |
| `SomeInf<int64>`  | `T`      | false   |

# infer_deep_nested_type_params

```dexscript
interface SomeInf {   
	<T>: interface{}
	Action1(arg: T)
}
```

```dexscript
interface AnotherInf {   
	<E1>: interface{}
	<E2>: interface{}
	Action2(index: '0', arg: E1)
	Action3(index: '1', arg: E2)
}
```

```dexscript
(<E1>: string, <E2>: string, arg0: AnotherInf<SomeInf<E1>, SomeInf<E2>>): E2
```

| posArgs                                  | func.ret | success | needRuntimeCheck |
| ---------------------------------------- | -------- | ------- | ---------------- |
| `AnotherInf<SomeInf<'a'>, SomeInf<'b'>>` | `string` | true    | false            |

# infer_two_direct_placeholders

```desxscript
(<T>: interface{}, left: T, right: T): T
```

| posArgs          | func.ret | success |
| ---------------- | -------- | ------- |
| `int64, int64`   | `int64`  | true    |
| `string, string` | `string` | true    |
| `string, int64`  | `T`      | false   |

# specify_type_args

```dexscript
(<T>: interface{}, left: T, right: T): bool
```

| typeArgs | posArgs          | success |
| -------- | ---------------- | ------- |
| `int64`  | `string, string` | false   |
| `int64`  | `int64, int64`   | true    |


