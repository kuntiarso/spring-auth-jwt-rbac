CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(191) NULL,
    `email` VARCHAR(191) NOT NULL,
    `password` TEXT NOT NULL,
    `refreshToken` VARCHAR(191) NULL,
    `verificationToken` VARCHAR(191) NULL,
    `otp` VARCHAR(6) NULL,
    `firstName` VARCHAR(191) NULL,
    `lastName` VARCHAR(191) NULL,
    `phone` VARCHAR(191) NULL,
    `profilePicture` VARCHAR(191) NULL,
    `role` ENUM('UNASSIGNED', 'ADMIN', 'ALUMNI', 'PARTNER') NOT NULL DEFAULT 'UNASSIGNED',
    `isVerified` BOOLEAN NOT NULL DEFAULT false,
    `verifiedDate` DATETIME(3) NULL,
    `createdAt` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updatedAt` DATETIME(3) NOT NULL,

    UNIQUE INDEX `users_username_key`(`username`),
    UNIQUE INDEX `users_email_key`(`email`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
