package com.thekdub.objects

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.PriorityBlockingQueue

class LDAPTaskQueue {
    private val taskQueue = PriorityBlockingQueue<LDAPTask>(16)
    private val taskIDMap = ConcurrentHashMap<Int, LDAPTask>(16)

    fun enqueue(task: LDAPTask) {
        if (taskQueue.offer(task)) {
            taskIDMap[task.messageID] = task
        }
    }

    fun cancelTaskByID(messageID: Int): Boolean {
        val task = taskIDMap.remove(messageID)
        return task?.let { taskQueue.remove(it) } ?: false
    }

    fun process(): Boolean {
        val task = taskQueue.remove()
        task?.let {  }
        return true;
    }



}