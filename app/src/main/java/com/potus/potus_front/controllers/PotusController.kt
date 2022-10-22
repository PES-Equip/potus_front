package com.potus.potus_front.controllers

interface PotusController {
    companion object {
        @JvmStatic
        fun getWater(): Int {
            // Here a call to the backend service will be performed to get the actual water level.
            return 0
        }

        fun getLeaves(): Int {
            // Here a call to the backend service will be performed to get the actual leaves count.
            return 0
        }

        fun updateWater(updatedWater: Int): Boolean {
            // Here a call to the backend service will be performed to set the water level.
            if(updatedWater in 0..100) return true
            return false
        }

        fun updateLeaves(updatedLeaves: Int): Boolean {
            // Here a call to the backend service will be performed to set the leaves count.
            if(updatedLeaves > 0) return true
            return false
        }
    }
}