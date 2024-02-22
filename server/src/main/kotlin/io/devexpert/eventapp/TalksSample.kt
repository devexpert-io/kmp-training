package io.devexpert.eventapp

import io.devexpert.eventapp.domain.SocialNetwork
import io.devexpert.eventapp.domain.Speaker
import io.devexpert.eventapp.domain.Talk

val sampleTalks = listOf(
    Talk(
        id = 1,
        title = "Kotlin Multiplatform",
        description = "Kotlin Multiplatform is a technology that allows you to use the same codebase for different platforms.",
        time = "10:00",
        speaker = Speaker(
            id = 1,
            name = "John Doe",
            bio = "John is a software engineer with a passion for Kotlin Multiplatform.",
            socialLinks = mapOf(
                SocialNetwork.LINKEDIN to "https://www.linkedin.com/in/johndoe",
                SocialNetwork.GITHUB to "https://github.com/johndoe"
            )
        )
    ),
    Talk(
        id = 2,
        title = "Introduction to Android Development",
        description = "This talk will cover the basics of Android development, including the Android Studio IDE and the Kotlin programming language.",
        time = "11:00",
        speaker = Speaker(
            id = 2,
            name = "Jane Smith",
            bio = "Jane is an experienced Android developer and a frequent speaker at tech conferences.",
            socialLinks = mapOf(
                SocialNetwork.LINKEDIN to "https://www.linkedin.com/in/janesmith",
                SocialNetwork.GITHUB to "https://github.com/janesmith"
            )
        )
    ),
    Talk(
        id = 3,
        title = "Advanced Gradle Techniques",
        description = "This talk will delve into advanced techniques for using Gradle, the build automation tool for Android development.",
        time = "12:00",
        speaker = Speaker(
            id = 3,
            name = "Bob Johnson",
            bio = "Bob is a senior software engineer who specializes in build systems and automation.",
            socialLinks = mapOf(
                SocialNetwork.LINKEDIN to "https://www.linkedin.com/in/bobjohnson",
                SocialNetwork.GITHUB to "https://github.com/bobjohnson"
            )
        )
    )
)

/*
[
    {
        "id": 1,
        "title": "Kotlin Multiplatform",
        "description": "Kotlin Multiplatform is a technology that allows you to use the same codebase for different platforms.",
        "time": "10:00",
        "speaker": {
            "id": 1,
            "name": "John Doe",
            "bio": "John is a software engineer with a passion for Kotlin Multiplatform.",
            "socialLinks": {
                "LINKEDIN": "https://www.linkedin.com/in/johndoe",
                "GITHUB": "https://github.com/johndoe"
            }
        }
    },
    {
        "id": 2,
        "title": "Introduction to Android Development",
        "description": "This talk will cover the basics of Android development, including the Android Studio IDE and the Kotlin programming language.",
        "time": "11:00",
        "speaker": {
            "id": 2,
            "name": "Jane Smith",
            "bio": "Jane is an experienced Android developer and a frequent speaker at tech conferences.",
            "socialLinks": {
                "LINKEDIN": "https://www.linkedin.com/in/janesmith",
                "GITHUB": "https://github.com/janesmith"
            }
        }
    },
    {
        "id": 3,
        "title": "Advanced Gradle Techniques",
        "description": "This talk will delve into advanced techniques for using Gradle, the build automation tool for Android development.",
        "time": "12:00",
        "speaker": {
            "id": 3,
            "name": "Bob Johnson",
            "bio": "Bob is a senior software engineer who specializes in build systems and automation.",
            "socialLinks": {
                "LINKEDIN": "https://www.linkedin.com/in/bobjohnson",
                "GITHUB": "https://github.com/bobjohnson"
            }
        }
    }
]
 */