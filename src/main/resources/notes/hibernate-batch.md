Took me a while to understand how hibernate batching works and what is the best strategy to go with when you're using MySQL.
For MySQL with the stuff we need to add for Hibernate batching are

```
hibernate:
  jdbc:
    batch_size: 50
  generate_statistics: true  #Just to see statistics
``` 

In the JDBC URL add (this is a performance enhancer for MySQL).

```
rewriteBatchedStatements=true
```

Although you’re using a single database round-trip, MySQL processes each statement individually. Hence, the above condition helps tremendously.
We can see the sql statements generated using the below in jdbc url.

```
profileSQL=true
```

The below conditions speak for themselves. A CSV file of 250k records. Chunk size of 5k.
```
With Hibernate batch size of 50 and rewriteBatchedStatements=true, it took 7m22s
With Hibernate batch size of 50, it took 8m30s
With none of the above, it took 8m40s
```

Further 2 links giving important info on what's to be done.

```
https://discourse.hibernate.org/t/hibernate-issuing-individual-insert-statements-even-though-batch-insert-is-enabled/2014/2
https://awesomeopensource.com/project/AnghelLeonard/Hibernate-SpringBoot
```

In MySQL we usually use the identity generator for the entities. But it doesn't support batching. Hence, we need to use some other kind of generator given at link below.
This increases the execution time tremendously. Hence, I would say it would be bad.

```
https://vladmihalcea.com/how-to-combine-the-hibernate-assigned-generator-with-a-sequence-or-an-identity-column/
```