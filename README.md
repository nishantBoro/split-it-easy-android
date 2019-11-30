# Split It Easy
Android app to split bills the easy way.

## Images

Look at the database deisgn first then app design for better understanding of the app

## App structure:




## Database Design:
We use three simple entities:


Hence, if a group is deleted/updated the associated bills and members of the group are deleted/updated too. 
Also, if a member is deleted/updated the associated bills of the member are deleted/updated too.

Room persistence library with View-Models and LiveData is used in this project. The different levels that any database operation goes through are:

ViewModelFactory(entry point) \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| \
ViewModel \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| \
Repository(Perform Queries in Async Mode) \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| \
Data access Object(Queries to insert,delete,..) \
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| \
Entity(columns) \


## Algorithm used for settling debts:

Calculate Balances: Find the balance of everyone in the group. Balance is the net amount of money someone owes or is owed from the group. Ex: \
Member Expenses(Contributions made by the member) \
----------- --------- \
A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;500 \
B&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;600 \
C&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;200 \
Total&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1300

Divide Total/noOfMembers = 1300/3=433.33(let's call this as eachPay). Hence, each member in the group was supposed to spend an expense of 433.33 bucks. Next, for each individual, subtract expenses(of the member) from eachPay. Ex: For member A, balance = eachPay - expenses of the member=433.33-500=-66.67. If the balance turns out to be -ve, it means the individual is owed money and hence added to the debtors list. If the balance turns out to be +ve, it means the individual owes money to the group and hence added to the creditors list. \

Final Balances: \
Member Expenses(Contributions made by the member) \
----------- --------- \
A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-66.67  
B&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-166.67\
C&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;233.33

Calculate Transactions: Pick the largest elements from the debtors and creditors list. Ex: If debtors = {4,3} and creditors={2,7}, pick 4 as the largest debtor and 7 as the largest creditor.Now, do a transaction between them. The debtor with a balance of 4 receives $4 from the creditor with a balance of 7 and hence, the debtor is eliminated from further transactions. Repeat the same steps until and unless there are no creditors and debtors.

Optimisation: This algorithm produces correct results but the no of transactions is not minimum. To minimize it, we could use the subset sum algorithm which is a NP problem. The use of a NP solution could really slow down the app!






