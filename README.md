# Split It Easy
Going on a road trip?.  Worried about all the maths you gotta do to find out "who owes whom and how much" after the trip?. Well, worry not!. Add all your expenses to this app and let it do the calculations for you.

# Download: 
https://github.com/nishant-boro/split-it-easy-android/releases/download/v1.1/splitItEasy.apk

### Also, available on, f-Droid: 
https://f-droid.org/en/packages/com.nishantboro.splititeasy/ 
### XDA Labs: 
https://labs.xda-developers.com/store/app/com.nishantboro.splititeasy 
### Amazon App Store: 
https://www.amazon.com/dp/B08273SN5W/ref=sr_1_1?_encoding=UTF8&qid=1575320141&refinements=p_4%3ANishant+Boro&s=mobile-apps&sr=1-1

## Features:
- Offline app
- No user login and registration required
- Uses a O(n) algorithm to settle debts
- Built in Java for Android Platform

## Android concepts used:
- Recycler views
- Fragments & ViewPager
- Activites & intents
- Room Persistence, LiveData, View-Model
- Different types of layouts

## App structure:
<img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_1.png" width="200"> <img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_2.png" width="200"> <img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_3.png" width="200">

<img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_4.png" width="200"> <img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_5.png" width="200"> <img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_6.png" width="200">

<img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_7.png" width="200"> <img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/screenshots/Screenshot_8.png" width="200">

## How to use it?
![](split_it_easy.gif)

## Database Design:
We use three simple entities:
<img src="https://github.com/nishant-boro/split-it-easy-android/blob/master/db_view.png" width="800">

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
Entity(columns)


## Algorithm used for settling debts:

Calculate Balances: Find the balance of everyone in the group. Balance is the net amount of money someone owes or is owed from the group. Ex:
### Initially: 
Member Expenses(Contributions made by the member) \
----------- --------- \
A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;500 \
B&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;600 \
C&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;200 \
Total&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1300

Divide Total/noOfMembers = 1300/3=433.33(let's call this as eachPay). Hence, each member in the group was supposed to spend an expense of 433.33 bucks. Next, for each individual, subtract expenses(of the member) from eachPay. Ex: For member A, balance = eachPay - expenses of the member=433.33-500=-66.67. If the balance turns out to be -ve, it means the individual is owed money and hence added to the debtors list. If the balance turns out to be +ve, it means the individual owes money to the group and hence added to the creditors list. \

### Final Balances:
Member Balances \
----------- --------- \
A&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-66.67  
B&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-166.67\
C&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;233.33

Calculate Transactions: Pick the largest elements from the debtors and creditors list. Ex: If debtors = {4,3} and creditors={2,7}, pick 4 as the largest debtor and 7 as the largest creditor.Now, do a transaction between them. The debtor with a balance of 4 receives $4 from the creditor with a balance of 7 and hence, the debtor is eliminated from further transactions. Repeat the same steps until and unless there are no creditors and debtors.

Optimisation: This algorithm produces correct results but the no of transactions is not minimum. To minimize it, we could use the subset sum algorithm which is a NP problem. The use of a NP solution could really slow down the app!


