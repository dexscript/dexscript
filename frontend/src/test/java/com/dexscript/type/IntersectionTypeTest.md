# assignable_relationship

| assignable | to               | from             |
| ---------- | ---------------- | ---------------- |
| true       | `string`         | `string & 'abc'` |
| false      | `string & 'abc'` | `string`         |
| true       | `'abc'`          | `string & 'abc'` |