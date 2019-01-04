# one_field

```dexscript
interface MyInf {
    getField(): string
    setField(value: string)
}
```

```dexscript
{
    field: 'hello'
}
```

# two_fields

```dexscript
interface MyInf {
    getField1(): string
    setField1(value: string)
    getField2(): int64
    setField2(value: int64)
}
```

```dexscript
{
    field1: 'hello',
    field2: 100
}
```

