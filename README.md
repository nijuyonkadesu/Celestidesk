# Celestidesk
Basic idea is: you create a request as a employee. It gets escalated to the teamlead. It is further escalated to the manager. If approved, the employee sees it in his approved list. Else in rejected list.
Uses this [beautiful backend](https://github.com/niyasrad/CelestiDesk-BE) by @niyasrad 

Multiple person can function under a specific role, and they can collectively approve or deny requests.
An option for emergency request is given. Because, unavoidable situations do occur.

There are 4 Roles: `EMPLOYEE`, `TEAM_LEAD`, `MANAGER` and `EMERGENCY`

## Request Screen
<p align="center">
<img src="previews/ss (1).png" alt="employee" width="270px" />
<img src="previews/ss (2).png" alt="creating a request" width="270px" />
</p>

## Approval & Transaction Screen 
<p align="center">
<img src="previews/ss (3).png" alt="making a decision on the request" width="270px" />
<img src="previews/ss (4).png" alt="history of decisions" width="270px" />
</p>

# Motivation
To eliminate the need for writing a letter, and getting it signed by faculties for the permission to leave the hostel. Yea, it's a one heck of work, and to maintain those paper records are another headache for the faculties.
Now, with one click, the request can be granted, and it's all well recorded and mailed to authorities every month. Students can now show the approved requests to the appropriate person before leaving the campus.
And yea, you can't cheat, I will stricken out the expired requests 🤭 so it is obvious.

## Stack
- [x] Retrofit - network client
- [x] ROOM's FTS - Full Text Searching
- [x] Dagger Hilt - Dependency Injection
- [x] flow & coroutines
- [x] M3 - Bottom bar, chips, input fields, etc
- [x] Repository Pattern - Architecture
- [x] MVVM - Architecture
- [x] Gradle Version Catalog

## Download APK
Go to the [Releases](https://github.com/nijuyonkadesu/Celestidesk/releases) to download the latest APK
Soon there will be a official playstore release

## Contribution 
Feel free to create issues, and send a pull request. A healthy discussion is what will make the community stronger.
1. Clone and setup the backend repository in local
2. Clone this repository and open in android studio
3. Fill the following details in `local.properties` file ('base_url' of the backend is the most important). Mail is not used within celestidesk app, will be removed soon
```properties
mail_id="xx@xx.xxx"
mail_pwd="xxxx"
mail_to="xx@xx.xxx"
base_url="https://somewhere.ontheworld.com/"
```
4. Compile and run
5. Don't forget to use Application Inspector option for debugging network