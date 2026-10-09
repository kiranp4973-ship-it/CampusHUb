package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CampusRepository {

    // Current Student Profile
    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "user_me",
            name = "Kiran Kumar",
            usnOrRoll = "1BI24CS089",
            email = "kirankumarp7349@gmail.com",
            college = "Bangalore Institute of Technology (BIT)",
            course = "Computer Science and Engineering",
            semester = 3,
            section = "A",
            role = UserRole.STUDENT,
            bio = "CS 3rd Sem | Android & Algorithms builder | Studying hard for DSA and OS internals",
            skills = listOf("Kotlin", "Java", "Data Structures", "Jetpack Compose", "Git"),
            studyInterests = listOf("DSA", "Systems Programming", "Cloud Systems"),
            status = FriendStatus.AVAILABLE,
            customStatusNote = "Prep in Library for OS Internal"
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Configurable Universal Institution Hierarchy
    private val _institutionHierarchy = MutableStateFlow(
        InstitutionHierarchy(
            country = "India",
            stateOrRegion = "Karnataka",
            universityOrBoard = "Visvesvaraya Technological University (VTU)",
            institutionName = "Bangalore Institute of Technology",
            campus = "Main Campus (K.R. Road, Bengaluru)",
            department = "Department of Computer Science & Engineering",
            program = "Bachelor of Engineering (B.E.)",
            courseOrBranch = "Computer Science and Engineering",
            academicYear = "2026-2027",
            termSystem = TermSystem.SEMESTER,
            semesterOrTerm = 3,
            sectionOrBatch = "Section A",
            gradingSystem = GradingSystem.CGPA_10_POINT,
            minAttendanceThreshold = 75.0
        )
    )
    val institutionHierarchy: StateFlow<InstitutionHierarchy> = _institutionHierarchy.asStateFlow()

    // Opportunities Hub (Local, National, Global)
    private val _opportunities = MutableStateFlow(
        listOf(
            OpportunityItem(
                id = "opp1",
                title = "Google Summer of Code 2027 Mentorship & Prep",
                organization = "Google Open Source",
                scope = DiscoveryScope.GLOBAL,
                category = "Open Source & Research",
                deadline = "Nov 15, 2026",
                stipendOrAward = "$1,500 - $3,000 USD Stipend",
                eligibility = "Enrolled undergraduate / university students worldwide",
                description = "Contribute to top open-source foundations (Linux Foundation, Apache, Android Open Source). CampusHub peer review circle active.",
                officialUrl = "https://summerofcode.withgoogle.com",
                isSaved = true
            ),
            OpportunityItem(
                id = "opp2",
                title = "Smart India Hackathon (SIH 2026) College Screening",
                organization = "Ministry of Education & AICTE",
                scope = DiscoveryScope.NATIONAL,
                category = "Hackathon",
                deadline = "Oct 25, 2026",
                stipendOrAward = "₹1,00,000 Grand Prize per Problem Statement",
                eligibility = "Team of 6 engineering students (min. 1 female participant)",
                description = "Nationwide innovation initiative providing students a platform to solve pressing problems of central ministries and industries.",
                officialUrl = "https://sih.gov.in",
                isSaved = false
            ),
            OpportunityItem(
                id = "opp3",
                title = "Campus Summer Research Fellowship in Systems",
                organization = "BIT Advanced Computing Research Lab",
                scope = DiscoveryScope.LOCAL,
                category = "Research",
                deadline = "Oct 30, 2026",
                stipendOrAward = "₹15,000 / month Stipend + Lab Credit",
                eligibility = "3rd and 5th Semester CSE/ISE students with CGPA >= 8.0",
                description = "Hands-on research under Dr. Rajesh Rao on Operating System kernel scheduling and distributed cloud execution.",
                officialUrl = "https://bit-bangalore.edu.in/research",
                isSaved = true
            ),
            OpportunityItem(
                id = "opp4",
                title = "DAAD WISE International Research Internship (Germany)",
                organization = "DAAD German Academic Exchange Service",
                scope = DiscoveryScope.GLOBAL,
                category = "Scholarship",
                deadline = "Nov 01, 2026",
                stipendOrAward = "€934 Monthly Allowance + Travel Subsidy",
                eligibility = "Students from selected Indian universities in engineering & sciences",
                description = "Funded research internships at top German universities and research institutes for 2-3 months.",
                officialUrl = "https://www.daad.de",
                isSaved = false
            )
        )
    )
    val opportunities: StateFlow<List<OpportunityItem>> = _opportunities.asStateFlow()

    // Campus Clubs & Student Societies
    private val _campusClubs = MutableStateFlow(
        listOf(
            CampusClubItem("club1", "ACM Student Chapter", "Technical", 142, "Premier student computing chapter organizing code sprints, guest lectures and mock interviews.", "Prof. Ananya Sharma & Sneha Patel", "Algorithmic Code Sprint on Saturday", true),
            CampusClubItem("club2", "Google Developer Student Clubs (GDSC)", "Technical", 210, "University community for mobile, web, cloud and machine learning developers.", "Rahul Verma", "Android Compose Workshop next week", true),
            CampusClubItem("club3", "Rotaract Club of BIT", "Social & Cultural", 95, "Community leadership, blood donation drives, and cultural student initiatives.", "Tanvi Deshmukh", "Annual Charity Run on Sunday", false),
            CampusClubItem("club4", "E-Cell (Entrepreneurship Cell)", "E-Cell", 118, "Startup incubator, pitch competitions, and angel investor networking sessions.", "Aditya Rao", "Campus Startup Pitch Deck Demo Day", false)
        )
    )
    val campusClubs: StateFlow<List<CampusClubItem>> = _campusClubs.asStateFlow()

    // Semester 3 Subjects
    private val _subjects = MutableStateFlow(
        listOf(
            Subject("sub_dsa", "CS301", "Data Structures & Applications", "DSA", "Prof. Ananya Sharma", "LH-204", 4, 0xFF2563EB),
            Subject("sub_os", "CS302", "Operating Systems", "OS", "Dr. Rajesh Rao", "LH-201", 4, 0xFFEA580C),
            Subject("sub_java", "CS303", "OOP with Java", "Java", "Prof. Priya Nair", "Lab 2 / LH-105", 3, 0xFF0D9488),
            Subject("sub_ddco", "CS304", "Digital Design & Computer Org", "DDCO", "Prof. Suresh Hegde", "LH-202", 3, 0xFF8B5CF6),
            Subject("sub_math", "MA301", "Probability & Statistics", "Maths", "Dr. Meenakshi Sundaram", "LH-301", 4, 0xFF0284C7)
        )
    )
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    // Attendance Records
    private val _attendance = MutableStateFlow(
        listOf(
            AttendanceRecord("sub_dsa", totalClasses = 34, presentCount = 30, requiredPercentage = 75.0),
            AttendanceRecord("sub_os", totalClasses = 31, presentCount = 23, requiredPercentage = 75.0), // 74.19% Warning!
            AttendanceRecord("sub_java", totalClasses = 28, presentCount = 26, requiredPercentage = 75.0),
            AttendanceRecord("sub_ddco", totalClasses = 30, presentCount = 24, requiredPercentage = 75.0),
            AttendanceRecord("sub_math", totalClasses = 35, presentCount = 29, requiredPercentage = 75.0)
        )
    )
    val attendance: StateFlow<List<AttendanceRecord>> = _attendance.asStateFlow()

    // Timetable Slots (Monday to Friday)
    private val _timetable = MutableStateFlow(
        listOf(
            // Today / Monday
            TimetableSlot("t1", 1, "09:00", "10:00", "sub_dsa", "LH-204", "Lecture"),
            TimetableSlot("t2", 1, "10:00", "11:00", "sub_os", "LH-201", "Lecture"),
            TimetableSlot("t3", 1, "11:30", "12:30", "sub_java", "Lab 2", "Lab"),
            TimetableSlot("t4", 1, "14:00", "15:00", "sub_ddco", "LH-202", "Lecture"),
            // Tuesday
            TimetableSlot("t5", 2, "09:00", "10:00", "sub_math", "LH-301", "Lecture"),
            TimetableSlot("t6", 2, "10:00", "11:00", "sub_dsa", "LH-204", "Lecture"),
            TimetableSlot("t7", 2, "11:30", "13:30", "sub_os", "Lab 3", "Lab"),
            TimetableSlot("t8", 2, "14:30", "15:30", "sub_java", "LH-105", "Lecture"),
            // Wednesday
            TimetableSlot("t9", 3, "09:00", "10:00", "sub_ddco", "LH-202", "Lecture"),
            TimetableSlot("t10", 3, "10:00", "11:00", "sub_math", "LH-301", "Lecture"),
            TimetableSlot("t11", 3, "11:30", "12:30", "sub_os", "LH-201", "Lecture"),
            TimetableSlot("t12", 3, "14:00", "16:00", "sub_dsa", "Lab 1", "Lab"),
            // Thursday
            TimetableSlot("t13", 4, "09:00", "10:00", "sub_java", "LH-105", "Lecture"),
            TimetableSlot("t14", 4, "10:00", "11:00", "sub_dsa", "LH-204", "Lecture"),
            TimetableSlot("t15", 4, "11:30", "12:30", "sub_math", "LH-301", "Lecture"),
            TimetableSlot("t16", 4, "14:00", "15:00", "sub_os", "LH-201", "Lecture"),
            // Friday
            TimetableSlot("t17", 5, "09:00", "10:00", "sub_os", "LH-201", "Lecture"),
            TimetableSlot("t18", 5, "10:00", "11:00", "sub_ddco", "LH-202", "Lecture"),
            TimetableSlot("t19", 5, "11:30", "13:30", "sub_java", "Lab 2", "Lab"),
            TimetableSlot("t20", 5, "14:30", "15:30", "sub_dsa", "LH-204", "Lecture")
        )
    )
    val timetable: StateFlow<List<TimetableSlot>> = _timetable.asStateFlow()

    // Study Tasks
    private val _tasks = MutableStateFlow(
        listOf(
            StudyTask("task1", "Complete DSA Unit 2: Stack Applications", "sub_dsa", 45, false),
            StudyTask("task2", "Submit Java Assignment 3 (Polymorphism)", "sub_java", 30, true),
            StudyTask("task3", "Revise OS CPU Scheduling Algorithms", "sub_os", 30, false),
            StudyTask("task4", "Prepare DDCO Karnaugh Map practice problems", "sub_ddco", 25, false)
        )
    )
    val tasks: StateFlow<List<StudyTask>> = _tasks.asStateFlow()

    // Syllabus
    private val _syllabi = MutableStateFlow(
        listOf(
            SubjectSyllabus(
                subjectId = "sub_dsa",
                modules = listOf(
                    SyllabusModule("mod1", 1, "Introduction to Data Structures & Arrays", listOf(
                        SyllabusTopic("top1", "Linear vs Non-linear Data Structures", TopicStatus.COMPLETED),
                        SyllabusTopic("top2", "Dynamic Memory Allocation", TopicStatus.COMPLETED),
                        SyllabusTopic("top3", "Sparse Matrix Representation", TopicStatus.COMPLETED)
                    )),
                    SyllabusModule("mod2", 2, "Stacks and Queues", listOf(
                        SyllabusTopic("top4", "Stack Operations & Array/Pointer implementation", TopicStatus.COMPLETED),
                        SyllabusTopic("top5", "Infix to Postfix conversion & evaluation", TopicStatus.STUDYING),
                        SyllabusTopic("top6", "Circular Queue & Priority Queue", TopicStatus.NOT_STARTED)
                    )),
                    SyllabusModule("mod3", 3, "Linked Lists", listOf(
                        SyllabusTopic("top7", "Singly & Doubly Linked Lists", TopicStatus.COMPLETED),
                        SyllabusTopic("top8", "Circular Linked Lists & Applications", TopicStatus.NOT_STARTED)
                    )),
                    SyllabusModule("mod4", 4, "Trees & Binary Search Trees", listOf(
                        SyllabusTopic("top9", "Binary Tree Traversals (Inorder, Preorder, Postorder)", TopicStatus.NOT_STARTED),
                        SyllabusTopic("top10", "BST insertion, deletion and searching", TopicStatus.NOT_STARTED)
                    )),
                    SyllabusModule("mod5", 5, "Graphs, Hashing and File Structures", listOf(
                        SyllabusTopic("top11", "BFS and DFS Graph Algorithms", TopicStatus.NOT_STARTED),
                        SyllabusTopic("top12", "Hash Functions & Collision Resolution", TopicStatus.NOT_STARTED)
                    ))
                )
            ),
            SubjectSyllabus(
                subjectId = "sub_os",
                modules = listOf(
                    SyllabusModule("os_m1", 1, "OS Overview & System Calls", listOf(
                        SyllabusTopic("os_t1", "Kernel Architectures & Dual Mode", TopicStatus.COMPLETED),
                        SyllabusTopic("os_t2", "Process Creation, fork() and exec()", TopicStatus.COMPLETED)
                    )),
                    SyllabusModule("os_m2", 2, "Process Synchronization & CPU Scheduling", listOf(
                        SyllabusTopic("os_t3", "FCFS, SJF, Round Robin & Priority", TopicStatus.STUDYING),
                        SyllabusTopic("os_t4", "Critical Section Problem & Semaphores", TopicStatus.NOT_STARTED),
                        SyllabusTopic("os_t5", "Banker's Deadlock Avoidance Algorithm", TopicStatus.NOT_STARTED)
                    )),
                    SyllabusModule("os_m3", 3, "Memory Management", listOf(
                        SyllabusTopic("os_t6", "Paging, Segmentation and TLB", TopicStatus.NOT_STARTED),
                        SyllabusTopic("os_t7", "Virtual Memory & Page Replacement (FIFO, LRU)", TopicStatus.NOT_STARTED)
                    ))
                )
            )
        )
    )
    val syllabi: StateFlow<List<SubjectSyllabus>> = _syllabi.asStateFlow()

    // Notes
    private val _notes = MutableStateFlow(
        listOf(
            NoteItem("note1", "DSA Unit 2: Detailed Handwritten Stack & Queue Notes", "sub_dsa", 2, "Kiran Kumar", "2026-10-06", "Covers expressions evaluation, infix-to-postfix algorithm step-by-step with solved diagrams.", "PDF", 34, 1, false, true, "Stack: LIFO (Last-In-First-Out). Standard functions: push(), pop(), peek(), isEmpty()."),
            NoteItem("note2", "Operating Systems - CPU Scheduling Formula Cheatsheet", "sub_os", 2, "Sneha Patel", "2026-10-04", "Turnaround time = Completion - Arrival. Waiting time = Turnaround - Burst time. Gantt chart tips.", "PDF", 45, 0, true, null, "CPU Scheduling summary sheet for Internal 1 revision."),
            NoteItem("note3", "Java OOP Principles & Exception Handling Summary", "sub_java", 1, "Rahul Verma", "2026-10-02", "Inheritance, Polymorphism, Abstract classes, Interface vs Abstract class difference table.", "PDF", 28, 2, false, null, "Java Object Oriented Programming comprehensive classroom notes."),
            NoteItem("note4", "DDCO Module 1 Karnaugh Maps and Boolean Simplification", "sub_ddco", 1, "Aditya Rao", "2026-09-28", "2, 3 and 4 variable K-Maps with POS and SOP examples and don't-care conditions.", "DOC", 19, 1, false, null, "Simplification examples with solved problems.")
        )
    )
    val notes: StateFlow<List<NoteItem>> = _notes.asStateFlow()

    // Exams & Internals
    private val _exams = MutableStateFlow(
        listOf(
            ExamItem("ex1", "Internal Assessment 1 - DSA", ExamType.INTERNAL_1, "sub_dsa", "2026-10-14", "09:30 AM", "LH-204", "Module 1 & Module 2 (Arrays, Stacks, Queues)", 50),
            ExamItem("ex2", "Internal Assessment 1 - OS", ExamType.INTERNAL_1, "sub_os", "2026-10-15", "09:30 AM", "LH-201", "Module 1 & CPU Scheduling of Module 2", 50),
            ExamItem("ex3", "Internal Assessment 1 - Java OOP", ExamType.INTERNAL_1, "sub_java", "2026-10-16", "09:30 AM", "LH-105", "Module 1 & Java Classes/Inheritance", 50),
            ExamItem("ex4", "Internal Assessment 1 - DDCO", ExamType.INTERNAL_1, "sub_ddco", "2026-10-17", "09:30 AM", "LH-202", "Module 1 & Combinational Circuits", 50),
            ExamItem("ex5", "Data Structures Lab Practical Test", ExamType.PRACTICAL, "sub_dsa", "2026-10-28", "02:00 PM", "Lab 1", "Programs 1 to 6 (Stack, Circular Queue, Linked List)", 50)
        )
    )
    val exams: StateFlow<List<ExamItem>> = _exams.asStateFlow()

    // Assignments
    private val _assignments = MutableStateFlow(
        listOf(
            AssignmentItem("as1", "Infix to Postfix Algorithm Implementation & Test Cases", "sub_dsa", "Write clean C/Java code to convert parenthesized arithmetic expressions.", "Due Tomorrow, 11:59 PM", AssignmentStatus.IN_PROGRESS),
            AssignmentItem("as2", "Banker's Algorithm Safety State Solver", "sub_os", "Implement matrix calculations for allocation, max, and available resources.", "Due Oct 18, 05:00 PM", AssignmentStatus.NOT_STARTED),
            AssignmentItem("as3", "Java Employee Hierarchy using Abstract Classes", "sub_java", "Demonstrate runtime polymorphism and method overriding.", "Submitted Yesterday", AssignmentStatus.COMPLETED)
        )
    )
    val assignments: StateFlow<List<AssignmentItem>> = _assignments.asStateFlow()

    // Lab Programs
    private val _labPrograms = MutableStateFlow(
        listOf(
            LabProgram(
                id = "lab1",
                labNumber = 1,
                subjectId = "sub_dsa",
                title = "Stack Implementation using Array",
                problemStatement = "Write a program in Java/C to simulate the working of a Stack of integers using an array. Provide operations: Push, Pop, Display, Check Overflow/Underflow.",
                language = "Java",
                code = """
class Stack {
    private int maxSize;
    private int[] stackArray;
    private int top;

    public Stack(int s) {
        maxSize = s;
        stackArray = new int[maxSize];
        top = -1;
    }
    public void push(int j) {
        if (top == maxSize - 1) {
            System.out.println("Stack Overflow!");
            return;
        }
        stackArray[++top] = j;
    }
    public int pop() {
        if (top == -1) {
            System.out.println("Stack Underflow!");
            return -1;
        }
        return stackArray[top--];
    }
    public void display() {
        for (int i = top; i >= 0; i--) {
            System.out.print(stackArray[i] + " ");
        }
        System.out.println();
    }
}
                """.trimIndent(),
                explanation = "A stack operates on Last-In, First-Out (LIFO). We keep a 'top' pointer initialized to -1. When an item is pushed, top is incremented. When popped, top is decremented.",
                expectedOutput = "Pushed 10, 20, 30\nStack contents: 30 20 10\nPopped: 30",
                difficulty = "Easy",
                isCompleted = true
            ),
            LabProgram(
                id = "lab2",
                labNumber = 2,
                subjectId = "sub_dsa",
                title = "Circular Queue with Array",
                problemStatement = "Develop a program to implement Circular Queue operations (Enqueue, Dequeue, Display) managing front and rear index wraparound modulo capacity.",
                language = "Java",
                code = """
class CircularQueue {
    int[] q;
    int front = -1, rear = -1, size;
    CircularQueue(int size) {
        this.size = size;
        q = new int[size];
    }
    void enqueue(int val) {
        if ((rear + 1) % size == front) {
            System.out.println("Queue is Full!");
            return;
        }
        if (front == -1) front = 0;
        rear = (rear + 1) % size;
        q[rear] = val;
    }
    int dequeue() {
        if (front == -1) {
            System.out.println("Queue Empty!");
            return -1;
        }
        int data = q[front];
        if (front == rear) { front = rear = -1; }
        else { front = (front + 1) % size; }
        return data;
    }
}
                """.trimIndent(),
                explanation = "In Circular Queue, after reaching the last index, the rear wraps around to index 0 using modulo arithmetic, preventing wasted space.",
                expectedOutput = "Enqueued 1, 2, 3\nDequeued 1\nFront now points to index 1",
                difficulty = "Medium",
                isCompleted = false
            ),
            LabProgram(
                id = "lab3",
                labNumber = 3,
                subjectId = "sub_os",
                title = "Round Robin CPU Scheduling Simulation",
                problemStatement = "Simulate Round Robin CPU scheduling with given time quantum. Calculate average turnaround time and average waiting time for N processes.",
                language = "Java",
                code = "// Simulates time slice allocation in cyclic order across ready queue processes.",
                explanation = "Each process is assigned a fixed time quantum. If execution isn't complete, it is preempted and put back at the tail of the queue.",
                expectedOutput = "Process | Burst | Waiting | TAT\nP1      | 5     | 6       | 11\nAvg Waiting Time: 5.33 ms",
                difficulty = "Hard",
                isCompleted = false
            )
        )
    )
    val labPrograms: StateFlow<List<LabProgram>> = _labPrograms.asStateFlow()

    // Books Library
    private val _books = MutableStateFlow(
        listOf(
            BookItem("b1", "Data Structures with C", "Seymour Lipschutz (Schaum's Outline)", "2nd Edition", "sub_dsa", "Standard college prescribed textbook for arrays, lists, stacks, trees and graphs with solved problems.", "Textbook", true),
            BookItem("b2", "Operating System Concepts", "Abraham Silberschatz, Peter B. Galvin", "10th Edition", "sub_os", "The classic 'Dinosaur' textbook covering process management, memory paging, file systems, and storage.", "Textbook", true),
            BookItem("b3", "Core Java: Fundamentals", "Cay S. Horstmann", "12th Edition", "sub_java", "In-depth reference manual for robust enterprise Java programming, generics and collections.", "Programming", true),
            BookItem("b4", "Computer Organization and Embedded Systems", "Carl Hamacher, Zvonko Vranesic", "6th Edition", "sub_ddco", "Hardware architectures, CPU datapath, pipelining, and memory hierarchies.", "Reference", false)
        )
    )
    val books: StateFlow<List<BookItem>> = _books.asStateFlow()

    // Previous Question Papers
    private val _questionPapers = MutableStateFlow(
        listOf(
            QuestionPaper("qp1", "sub_dsa", "Semester End Exam", 2024, 3, listOf("AVL Tree Rotations", "Dijkstra Algorithm", "Infix to Postfix", "Binary Tree Traversals")),
            QuestionPaper("qp2", "sub_dsa", "Semester End Exam", 2023, 3, listOf("Circular Queue", "DFS vs BFS", "Sparse Matrix", "Threaded Binary Tree")),
            QuestionPaper("qp3", "sub_os", "Semester End Exam", 2024, 3, listOf("Banker's Algorithm", "Dining Philosophers", "Demand Paging", "Page Replacement (LRU vs FIFO)")),
            QuestionPaper("qp4", "sub_os", "Internal Assessment 1", 2024, 3, listOf("Fork system call output prediction", "FCFS vs Round Robin Gantt chart", "Semaphores")),
            QuestionPaper("qp5", "sub_java", "Semester End Exam", 2024, 3, listOf("Multithreading lifecycle", "Custom Exceptions", "Interface vs Abstract Class", "JDBC Connectivity"))
        )
    )
    val questionPapers: StateFlow<List<QuestionPaper>> = _questionPapers.asStateFlow()

    // Announcements
    private val _announcements = MutableStateFlow(
        listOf(
            Announcement("anc1", "Internal Assessment 1 Timetable Released", "IA-1 for 3rd semester CSE begins on October 14, 2026. Hall tickets and seating arrangements will be published on Friday. Attendance eligibility threshold is 75%.", "Today, 08:30 AM", "Prof. K. Venkatesh", "HOD - CSE", "Department", false),
            Announcement("anc2", "DSA Lab Record Submission Guidelines", "All students must submit completed observation books and printed code runs for experiments 1 to 5 by this Friday 4:00 PM.", "Yesterday", "Prof. Ananya Sharma", "Course Coordinator", "Class", true),
            Announcement("anc3", "Smart India Hackathon College Internal Screening", "Teams of 6 (min 1 female candidate) can register for campus round on portal before Oct 20.", "Oct 05, 2026", "Campus Tech Cell", "College Admin", "College", true)
        )
    )
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    // Friends and Classmates
    private val _friends = MutableStateFlow(
        listOf(
            UserProfile("u1", "Sneha Patel", "1BI24CS092", "sneha.p@bit.ac.in", null, "BIT", "Computer Science", 3, "A", UserRole.CLASS_REPRESENTATIVE, "Class Rep | Loves Java & Python", listOf("Java", "Python", "DSA"), listOf("Web Dev", "DSA"), FriendStatus.LIBRARY, "Reading OS Galvin Chapter 4"),
            UserProfile("u2", "Rahul Verma", "1BI24CS078", "rahul.v@bit.ac.in", null, "BIT", "Computer Science", 3, "A", UserRole.STUDENT, "Competitive programmer & open source enthusiast", listOf("C++", "DSA", "SQL"), listOf("Competitive Programming"), FriendStatus.IN_CLASS, "DSA Lecture in LH-204"),
            UserProfile("u3", "Aditya Rao", "1BI24CS015", "aditya.r@bit.ac.in", null, "BIT", "Computer Science", 3, "A", UserRole.STUDENT, "Full-stack mobile dev | Flutter & Kotlin", listOf("Kotlin", "Flutter", "Firebase"), listOf("Mobile Apps", "System Design"), FriendStatus.LAB, "Testing Java Socket client"),
            UserProfile("u4", "Tanvi Deshmukh", "1BI24CS110", "tanvi.d@bit.ac.in", null, "BIT", "Computer Science", 3, "A", UserRole.STUDENT, "AI researcher & statistics fan", listOf("Python", "Maths", "R"), listOf("Machine Learning", "Statistics"), FriendStatus.CANTEEN, "Grabbing lunch before DDCO class"),
            UserProfile("u5", "Manoj Kumar", "1BI24CS054", "manoj.k@bit.ac.in", null, "BIT", "Computer Science", 3, "B", UserRole.STUDENT, "Hardware & IoT builder", listOf("Arduino", "C", "Verilog"), listOf("Embedded Systems"), FriendStatus.AVAILABLE, "Free till 2 PM")
        )
    )
    val friends: StateFlow<List<UserProfile>> = _friends.asStateFlow()

    // Study Groups
    private val _studyGroups = MutableStateFlow(
        listOf(
            StudyGroup("grp1", "DSA Internal 1 Prep Squad", "sub_dsa", 14, "Solving stack, queue, recursion and previous VTU paper questions together.", "Aditya: Who solved question 3 from 2023 paper?", "10:14 AM"),
            StudyGroup("grp2", "OS CPU Scheduling & Deadlock", "sub_os", 8, "Daily group study for numerical problems and Banker's algorithm.", "Sneha: Formulas cheat sheet uploaded in shared notes!", "09:45 AM"),
            StudyGroup("grp3", "BIT 3rd Sem CSE Official Group", "sub_all", 62, "Class-wide study group for class updates, reminders and group projects.", "Prof. Sharma announced lab quiz next week.", "Yesterday")
        )
    )
    val studyGroups: StateFlow<List<StudyGroup>> = _studyGroups.asStateFlow()

    // Chat Messages for current open chat
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("m1", "u1", "Sneha Patel", "Hey Kiran, did you complete the Java Assignment 3?", "09:30 AM", isFromMe = false),
            ChatMessage("m2", "user_me", "Kiran Kumar", "Yes, just submitted it yesterday night! I used abstract classes for Employee hierarchy.", "09:32 AM", isFromMe = true),
            ChatMessage("m3", "u1", "Sneha Patel", "Awesome! Can you share the UML diagram or notes?", "09:35 AM", isFromMe = false),
            ChatMessage("m4", "user_me", "Kiran Kumar", "Sure! Check the Notes tab under Java, I published the full summary notes there.", "09:36 AM", isFromMe = true),
            ChatMessage("m5", "u1", "Sneha Patel", "Super helpful, thanks! Let's do group revision in library around 4 PM.", "09:38 AM", isFromMe = false)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Lost and Found
    private val _lostAndFound = MutableStateFlow(
        listOf(
            LostAndFoundItem("lf1", isLost = true, title = "Casio fx-991EX Calculator", category = "Calculator", location = "Left in LH-204 3rd row desk", date = "Yesterday, 3:30 PM", description = "Black scientific calculator with 'Kiran' label etched on the back cover.", contactMethod = "WhatsApp: +91 9876543210 or meet in Section A", postedByName = "Kiran Kumar"),
            LostAndFoundItem("lf2", isLost = false, title = "Blue College ID Card & Lanyard", category = "ID card", location = "Found near Canteen stairs", date = "Today, 10:00 AM", description = "Student ID belongs to 3rd Sem ECE student Rohit M. Submitted to security room.", contactMethod = "Collect from Main Gate Security or Ping here", postedByName = "Sneha Patel"),
            LostAndFoundItem("lf3", isLost = true, title = "SanDisk 64GB USB Drive", category = "USB drive", location = "CS Lab 2 machine #14", date = "Oct 05, 2026", description = "Contains OS lab project code and installation ISO.", contactMethod = "Rahul V (CS-A)", postedByName = "Rahul Verma")
        )
    )
    val lostAndFound: StateFlow<List<LostAndFoundItem>> = _lostAndFound.asStateFlow()

    // Project Team Postings
    private val _projectTeams = MutableStateFlow(
        listOf(
            ProjectTeamPost("prj1", "AI Smart Campus Navigation App", listOf("Android", "Kotlin", "Jetpack Compose", "Mapbox"), 2, "Building an indoor navigation and class locator app for BIT campus. Need one Android UI developer and one backend developer.", "Kiran Kumar", "CSE - Sem 3 A"),
            ProjectTeamPost("prj2", "Automated Attendance System using Face Recognition", listOf("Python", "OpenCV", "Flask", "React"), 1, "Mini project for software engineering. Need a member proficient with Python and database management.", "Aditya Rao", "CSE - Sem 3 A"),
            ProjectTeamPost("prj3", "Library Book Rental & Peer Exchange Web App", listOf("Node.js", "MongoDB", "UI/UX"), 2, "Peer-to-peer textbook sharing platform so students can lend and borrow semester books.", "Sneha Patel", "CSE - Sem 3 A")
        )
    )
    val projectTeams: StateFlow<List<ProjectTeamPost>> = _projectTeams.asStateFlow()

    // Expense Groups
    private val _expenses = MutableStateFlow(
        ExpenseGroup(
            id = "exp1",
            title = "Nandi Hills Weekend College Trip",
            members = listOf("Kiran", "Aditya", "Rahul", "Sneha"),
            expenses = listOf(
                ExpenseItem("e1", "Breakfast & Coffee", 640.0, "Kiran", listOf("Kiran", "Aditya", "Rahul", "Sneha"), "Oct 05"),
                ExpenseItem("e2", "Cab & Toll charges", 1200.0, "Aditya", listOf("Kiran", "Aditya", "Rahul", "Sneha"), "Oct 05"),
                ExpenseItem("e3", "Snacks & Mineral Water", 360.0, "Sneha", listOf("Kiran", "Aditya", "Rahul", "Sneha"), "Oct 05")
            )
        )
    )
    val expenses: StateFlow<ExpenseGroup> = _expenses.asStateFlow()

    // Academic Calendar Events
    private val _calendarEvents = MutableStateFlow(
        listOf(
            AcademicEvent("ev_curr_asgn", "Java OOP Assignment Due", "2026-10-09", AcademicEventType.ASSIGNMENT, "11:59 PM", null, "Submit Polymorphism code & PDF report on portal", "Online LMS", "sub_java"),
            AcademicEvent("ev_curr_hack", "AI Hackathon Team Briefing", "2026-10-10", AcademicEventType.COLLEGE_EVENT, "11:00 AM", "12:30 PM", "Orientation for campus hackathon participants", "Auditorium Hall B", null),
            AcademicEvent("ev_study_grp", "DSA Group Revision & Practice", "2026-10-11", AcademicEventType.PERSONAL_TASK, "03:00 PM", "05:00 PM", "Stack & Queue previous papers problem solving", "Central Library Discussion Room 2", "sub_dsa"),
            AcademicEvent("ev1", "DSA Internal Assessment 1", "2026-10-14", AcademicEventType.INTERNAL, "09:30 AM", "11:00 AM", "Syllabus: Module 1 & 2 (Arrays, Stacks, Queues)", "LH-204", "sub_dsa"),
            AcademicEvent("ev2", "OS Internal Assessment 1", "2026-10-15", AcademicEventType.INTERNAL, "09:30 AM", "11:00 AM", "Syllabus: Module 1 & CPU Scheduling of Module 2", "LH-201", "sub_os"),
            AcademicEvent("ev3", "Java OOP Internal Assessment 1", "2026-10-16", AcademicEventType.INTERNAL, "09:30 AM", "11:00 AM", "Syllabus: Module 1 & Classes, Inheritance", "LH-105", "sub_java"),
            AcademicEvent("ev4", "DDCO Internal Assessment 1", "2026-10-17", AcademicEventType.INTERNAL, "09:30 AM", "11:00 AM", "Syllabus: Module 1 & Combinational circuits", "LH-202", "sub_ddco"),
            AcademicEvent("ev5", "DSA Lab Practical Test", "2026-10-28", AcademicEventType.LAB_SUBMISSION, "02:00 PM", "05:00 PM", "Observation book + Lab program execution", "Lab 1", "sub_dsa"),
            AcademicEvent("ev6", "Deepavali Festival Holiday", "2026-10-31", AcademicEventType.HOLIDAY, null, null, "College closed for festival - No lectures", "Campus Wide", null),
            AcademicEvent("ev7", "Karnataka Rajyotsava Holiday", "2026-11-01", AcademicEventType.HOLIDAY, null, null, "State holiday - College closed", "Campus Wide", null)
        )
    )
    val calendarEvents: StateFlow<List<AcademicEvent>> = _calendarEvents.asStateFlow()

    // User Actions
    fun markAttendance(subjectId: String, isPresent: Boolean) {
        _attendance.update { list ->
            list.map { record ->
                if (record.subjectId == subjectId) {
                    val newTotal = record.totalClasses + 1
                    val newPresent = if (isPresent) record.presentCount + 1 else record.presentCount
                    record.copy(totalClasses = newTotal, presentCount = newPresent)
                } else record
            }
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
        }
    }

    fun addTask(title: String, subjectId: String, minutes: Int) {
        val newTask = StudyTask("task_${System.currentTimeMillis()}", title, subjectId, minutes, false)
        _tasks.update { listOf(newTask) + it }
    }

    fun updateTopicStatus(subjectId: String, moduleId: String, topicId: String, newStatus: TopicStatus) {
        _syllabi.update { list ->
            list.map { syl ->
                if (syl.subjectId == syl.subjectId) {
                    val updatedModules = syl.modules.map { mod ->
                        if (mod.id == moduleId) {
                            val updatedTopics = mod.topics.map { top ->
                                if (top.id == topicId) top.copy(status = newStatus) else top
                            }
                            mod.copy(topics = updatedTopics)
                        } else mod
                    }
                    syl.copy(modules = updatedModules)
                } else syl
            }
        }
    }

    fun voteNoteUseful(noteId: String, isUseful: Boolean) {
        _notes.update { list ->
            list.map { note ->
                if (note.id == noteId) {
                    if (isUseful) {
                        note.copy(usefulCount = note.usefulCount + 1, isUserVotedUseful = true)
                    } else {
                        note.copy(notUsefulCount = note.notUsefulCount + 1, isUserVotedUseful = false)
                    }
                } else note
            }
        }
    }

    fun addNote(title: String, subjectId: String, module: Int, desc: String, content: String) {
        val newNote = NoteItem(
            id = "note_${System.currentTimeMillis()}",
            title = title,
            subjectId = subjectId,
            moduleNumber = module,
            uploadedBy = _currentUser.value.name,
            date = "Today",
            description = desc,
            fileType = "TEXT",
            usefulCount = 1,
            notUsefulCount = 0,
            isBookmarked = false,
            contentText = content
        )
        _notes.update { listOf(newNote) + it }
    }

    fun sendChatMessage(text: String) {
        val msg = ChatMessage(
            id = "m_${System.currentTimeMillis()}",
            senderId = _currentUser.value.id,
            senderName = _currentUser.value.name,
            content = text,
            timestamp = "Just now",
            isFromMe = true
        )
        _chatMessages.update { it + msg }
    }

    fun updateUserStatus(status: FriendStatus, note: String) {
        _currentUser.update { it.copy(status = status, customStatusNote = note) }
    }

    fun addLostAndFoundItem(isLost: Boolean, title: String, category: String, location: String, desc: String, contact: String) {
        val item = LostAndFoundItem(
            id = "lf_${System.currentTimeMillis()}",
            isLost = isLost,
            title = title,
            category = category,
            location = location,
            date = "Today",
            description = desc,
            contactMethod = contact,
            postedByName = _currentUser.value.name
        )
        _lostAndFound.update { listOf(item) + it }
    }

    fun addExpense(title: String, amount: Double, paidBy: String) {
        val group = _expenses.value
        val expense = ExpenseItem(
            id = "e_${System.currentTimeMillis()}",
            title = title,
            amount = amount,
            paidBy = paidBy,
            splitBetween = group.members,
            date = "Today"
        )
        _expenses.update { it.copy(expenses = it.expenses + expense) }
    }

    fun markAnnouncementRead(id: String) {
        _announcements.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    // Timetable & Calendar Event Management
    fun addAcademicEvent(event: AcademicEvent) {
        _calendarEvents.update { it + event }
    }

    fun updateAcademicEvent(event: AcademicEvent) {
        _calendarEvents.update { list ->
            list.map { if (it.id == event.id) event else it }
        }
    }

    fun deleteAcademicEvent(eventId: String) {
        _calendarEvents.update { list ->
            list.filterNot { it.id == eventId }
        }
    }

    fun addTimetableSlot(slot: TimetableSlot) {
        _timetable.update { it + slot }
    }

    fun updateTimetableSlot(slot: TimetableSlot) {
        _timetable.update { list ->
            list.map { if (it.id == slot.id) slot else it }
        }
    }

    fun deleteTimetableSlot(slotId: String) {
        _timetable.update { list ->
            list.filterNot { it.id == slotId }
        }
    }

    fun toggleClassCancelled(slotId: String) {
        _timetable.update { list ->
            list.map { if (it.id == slotId) it.copy(isCancelled = !it.isCancelled) else it }
        }
    }

    fun updateInstitutionHierarchy(newHierarchy: InstitutionHierarchy) {
        _institutionHierarchy.value = newHierarchy
    }

    fun toggleOpportunitySaved(opportunityId: String) {
        _opportunities.update { list ->
            list.map { if (it.id == opportunityId) it.copy(isSaved = !it.isSaved) else it }
        }
    }

    fun toggleClubJoined(clubId: String) {
        _campusClubs.update { list ->
            list.map { if (it.id == clubId) it.copy(isJoined = !it.isJoined) else it }
        }
    }
}
