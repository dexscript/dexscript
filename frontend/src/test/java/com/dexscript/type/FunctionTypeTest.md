# same_function_is_assignable


| assignable | to                | from              |
| ---------- | ----------------- | ----------------- |
| true       | `hello(): string` | `hello(): string` |
| true       | `hello(): string` | `hello(): string` |
| false      | `hello(): string` | `string`          |

# name_not_assignable


| assignable | to                | from              |
| ---------- | ----------------- | ----------------- |
| false      | `hello(): string` | `world(): string` |
| false      | `world(): string` | `hello(): string` |

# params_count_not_assignable

| assignable | to                            | from                          |
| ---------- | ----------------------------- | ----------------------------- |
| false      | `hello(): string`             | `hello(arg0: string): string` |
| false      | `hello(arg0: string): string` | `hello(): string`             |

# param_name_not_assignable


| assignable | to                | from              |
| ---------- | ----------------- | ----------------- |
| false      | `hello(a: int64)` | `hello(b: int64)` |
| false      | `hello(b: int64)` | `hello(a: int64)` |

# param_type_not_assignable

| assignable | to                 | from               |
| ---------- | ------------------ | ------------------ |
| false      | `hello(a: int64)`  | `hello(a: string)` |
| false      | `hello(a: string)` | `hello(a: int64)`  |

# ret_not_assignable

| assignable | to                | from              |
| ---------- | ----------------- | ----------------- |
| false      | `hello(): int64`  | `hello(): string` |
| false      | `hello(): string` | `hello(): int64`  |

# param_is_sub_type


| assignable | to                       | from                     |
| ---------- | ------------------------ | ------------------------ |
| false      | `hello(arg0: string)`    | `hello(arg0: 'example')` |
| true       | `hello(arg0: 'example')` | `hello(arg0: string)`    |

# ret_is_sub_type


| assignable | to                   | from                 |
| ---------- | -------------------- | -------------------- |
| true       | `hello(): string`    | `hello(): 'example'` |
| false      | `hello(): 'example'` | `hello(): string`    |