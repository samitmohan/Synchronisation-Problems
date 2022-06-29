# Synchronisation Problems

## Producer Consumer Problem.

Producer-Consumer Problem is also known as bounded buffer problem. The Producer-Consumer Problem is one of the classic problems of synchronization.

There is a buffer of N slots and each slot is capable of storing one unit of data.
There are two processes running, i.e. Producer and Consumer, which are currently operated in the buffer.

### There are certain restrictions/conditions for both the producer and consumer process, so that data synchronization can be done without interruption. These are as follows:

1) The producer tries to insert data into an empty slot of the buffer.
2) The consumer tries to remove data from a filled slot in the buffer.
3) The producer must not insert data when the buffer is full.
4) The consumer must not remove data when the buffer is empty.
5) The producer and consumer should not insert and remove data simultaneously.

![Alt text](/images/prodconsumer.png "PC")

## Algorithm

void Producer() {
    do {
        // wait until empty > 0
        wait(Empty);
        wait(mutex);
        add()
        signal(mutex);
        signal(Full);
   } while(TRUE);
}

void Consumer() {
    do {
        // wait until empty > 0
        wait(full);
        wait(mutex);
        consume()
        signal(mutex);
        signal(empty);
   } while(TRUE);
}

## Questions
Can the consumer remove an item from the buffer if full() = 0?
    No, if there are no items in the buffer, i.e. full semaphore  = 0, the consumer cannot remove any item.

Can the producer and consumer acquire the lock simultaneously?
    No, either the producer or consumer can acquire the lock at a time. The operation wait(mutex) is used to acquire the lock.


Both producer and consumer may try to update the queue at the same time. This could lead to data loss or inconsistencies.
Producers might be slower than consumers. In such cases, the consumer would process elements fast and wait.
In some cases, the consumer can be slower than a producer. This situation leads to a queue overflow issue.
In real scenarios, we may have multiple producers, multiple consumers, or both. This may cause the same message to be processed by different consumers.


--------------------------------------------------

## Dining Philosopher Problem

The Dining Philosophers Problem is a classic resource-sharing synchronization problem. It is particularly used for situations, where multiple resources need to be allocated.

There are five philosophers sitting around a circular dining table. The table has a bowl of spaghetti and five chopsticks.

At any given time, a philosopher will either think or eat. For eating, he uses two chopsticks- one from his left and another from his right. When a philosopher thinks, he keeps down both the chopsticks at their place.

A fork can be held by only one philosopher at any given time i.e. no two philosophers can use the same fork.  After a philosopher finishes eating, they put the forks back to their original places. A philosopher can only eat when both the forks are available to him, i.e. forks on his left and right should not be used by any other philosopher at the same time.

The problem is to design an effective algorithm, such that no philosopher has to starve, i.e. he can continue thinking and eating alternately for an indefinite amount of time.

Let us consider the philosophers to be processed in an OS and the chopsticks to be shared resources. Now if we observe clearly, each process needs two resources, out of which, one of the resources it has already acquired, but another resource is in use for another process. Due to this, till the time the other process does not release its resource, the current process cannot proceed further.

In turn, the other processes are dependent on another process for its resources and this goes on and on.Therefore, every process is waiting for some other process, hence they are in a circular wait. This leads the system to a deadlock.

![Alt text](/images/dp.png "DP")

## Algorithm

do {
   wait( chopstick[i] );
   wait( chopstick[ (i+1) % 5] );
   // eating
   
   signal( chopstick[i] );
   signal( chopstick[ (i+1) % 5] );
   
   // thinking
   
} while(1);


### Problems
1) Think until the left fork is available; when it is, pick it up;
2) Think until the right fork is available; when it is, pick it up;

when both forks are held, eat for a fixed amount of time;

3) Put the left fork down;
4) Put the right fork down;
5) Repeat from the beginning.


# Reader Writer Problem.

Consider a situation where we have a file shared between many people. 

If one of the people tries editing the file, no other person should be reading or writing at the same time, otherwise changes will not be visible to him/her.
However if some person is reading the file, then others may read it at the same time.
Precisely in OS we call this situation as the readers-writers problem 

### Problems

One set of data is shared among a number of processes
1) Once a writer is ready, it performs its write. Only one writer may write at a time
2) If a process is writing, no other process can read it
3) If at least one reader is reading, no other process can write
4) Readers may not write and only read


![Alt text](/images/rw.png "RW")

## Algorithm

do {
    // writer requests for critical section
    wait(wrt);  
   
    // performs the write

    // leaves the critical section
    signal(wrt);
} while(true);