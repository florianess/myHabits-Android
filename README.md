# myHabits-Android

## Structure

```
app/
├── java/                         
│   ├── data/                     
│   │   ├── dao/                    # Direct Accees Object (from SQL request)
│   │   ├── entities/               # Define Room entities
│   │   ├── AppDatabase             # Create DB
│   │   └── AppRepository           # Link DAOs, interface for UI component
│   ├── ui/                         # api files
│   │   ├── home/          
│   │   |   ├── HabitsListAdapter   # Display recycleview items
│   │   |   ├── HomeFragment        # Home fragment (page)
│   │   |   └── HomeViewModel       # Connexion with repository
│   │   ├── stats/                      
│   │   |   ├── StatsFragment       # Stats fragment (page)
│   │   |   └── StatsViewModel      # Connexion with repository
│   └── MainActivity                # Main activity class
├── res/
│   ├── layout/                
│   │   ├── activity_main.xml       # Main activity with fragment frame
│   │   ├── fragment_home.xml       # Home page (in main activity)
│   │   ├── fragment_stats.xml      # Stats page (in main activity)
│   │   └── habit_item.xml          # utils files
│   ├── menu/
│   │   └── bottom_nav_menu.xml     # Bottom nav bar menu
```

## TODO
- [ ] Background color habits
- [ ] Disable click for futur habits
- [ ] Stats page
