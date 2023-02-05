package jvm

// Memory Model -
// specification on circumstances when result of writing to a variable becomes seen by other threads

// JMM describes threads behaviour in JVM

// Results of operations very rarely appear in memory immediately
// due to caches and optimizations in CPU.

// Happens-before relation -
// if operation A happens before B then B sees all changes in memory executed by A.
// But it doesn't guarantee the order that A will be before B.


// - Executing in sequential order:
//    each operation in a thread happens before any next operation

// - Monitor blocking:
//    monitor unlocking happens before the next locking

// - volatile fields:
//    writing to a volatile field happens before any other reading from it

// - thread launch:
//    call of start() in a thread happens before any other operation in a launching thread

// - thread terminating:
//    any operation in a thread happens before its join() call in another thread

// - transitivity
//    if operation A is executing before B, and B before C, then A is before C


