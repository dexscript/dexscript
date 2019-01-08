# no_type_param

```dexscript
function Hello(): string {
    return 'hello'
}
```

```dexscript
interface MyInf {
    Consume__(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `string`   | `Hello()`      |
| true       | `MyInf`   | `new Hello()`      |

# one_type_param

```dexscript
function Hello(<T>: interface{}): T {
    return ''
}
```

```dexscript
interface MyInf {
    Consume__(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `string`   | `Hello<string>()`      |
| true       | `MyInf`   | `new Hello<string>()`      |

# await_consumer_without_type_param

```dexscript
function Hello() {
    await {
    case Say(): string {
        return ''
    }}
}
```

```dexscript
interface MyInf {
    Consume__(): void
    Say(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `MyInf`   | `new Hello()`   |

# await_consumer_with_type_param

```dexscript
function Hello(<T1>: interface{}, <T2>: interface{}): T1 {
    await {
    case Say(): T2 {
        return ''
    }}
    return ''
}
```

```dexscript
interface MyInf {
    Consume__(): int64
    Say(): string
}
```

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `MyInf`   | `new Hello<int64, string>()`   |