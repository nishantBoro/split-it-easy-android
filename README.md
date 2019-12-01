# Split It Easy
Android app to split bills the easy way.

## Images

Look at the database deisgn first then app design for better understanding of the app

## App structure:




## Database Design:
We use three entities:
GroupEntity:
Id(PK) | GroupName(Unique)

MemberEntity:
Id(PK) | GroupName(FK reference GroupEntity(GroupName)) | MemberName | MemberAvatar 

BillEntity:
Id(PK) | MemberId(FK reference MemberEntity(Id) | Item | PaidBy | Cost | Currency | GroupName(FK reference GroupEntity(GroupName))

Hence, if a group is deleted/updated the associated bills and members of the group are deleted/updated too. 
Also, if a member is deleted/updated the associated bills of the member are deleted/updated too.

Room persistence is used to build the database. The structure of the database is:

Entity(columns)
  |
Data access Object(Queries to insert,delete,..)
  |
Repository(Perform queries in Async Mode)
  |
ViewModel
  |
ViewModelFactory(entry point to the database)

## Algorithm used for splitting bills:

Calculate Balances: 

Calculate Transactions: Pick the largest element from debtors and creditors. Ex: If debtors = {4,3} and creditors={2,7}, pick 4 as the largest debtor and 7 as the largest creditor.
Now, do a transaction between them. The debtor with a balance of 4 receives $4 from the creditor with a balance of 7 and hence, the debtor is eliminated from further transactions. Repeat the same 
thing until and unless there are no creditors and debtors.
The priority queues help us find the largest creditor and debtor in constant time. However, adding/removing a member takes O(log n) time to perform it.
Optimisation: This algorithm produces correct results but the no of transactions is not minimum. To minimize it, we could use subset sum algorithm which is a NP problem.
But the use of a NP solution could really slow down the app.






