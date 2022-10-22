package com.potus.potus_front.controllers

interface PotusController {
    companion object {
        @JvmStatic
        fun getWater(): Int {
            return 80
        }

        fun getLeaves(): Int {
            return 0
        }

        fun updateWater(updatedWater: Int): Boolean {
            if(updatedWater in 1..99) return true
            return false
        }

        fun updateLeaves(updatedLeaves: Int): Boolean {
            if(updatedLeaves > 0) return true
            return false
        }
    }
}