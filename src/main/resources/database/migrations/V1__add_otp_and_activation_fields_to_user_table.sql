-- Migration: Add OTP and activation fields to users table
-- Target Table: [User]
-- Fields:
--   1. otp_code        : NVARCHAR(50), nullable - Stores OTP verification code
--   2. otp_expires_at  : DATETIME2, nullable    - Stores expiration timestamp of the OTP
--   3. is_activated    : BIT, not null, default 0 - Account activation status flag

IF NOT EXISTS (
    SELECT 1 FROM sys.columns 
    WHERE object_id = OBJECT_ID(N'[dbo].[User]') AND name = 'otp_code'
)
BEGIN
    ALTER TABLE [dbo].[User] ADD [otp_code] NVARCHAR(50) NULL;
END;

IF NOT EXISTS (
    SELECT 1 FROM sys.columns 
    WHERE object_id = OBJECT_ID(N'[dbo].[User]') AND name = 'otp_expires_at'
)
BEGIN
    ALTER TABLE [dbo].[User] ADD [otp_expires_at] DATETIME2 NULL;
END;

IF NOT EXISTS (
    SELECT 1 FROM sys.columns 
    WHERE object_id = OBJECT_ID(N'[dbo].[User]') AND name = 'is_activated'
)
BEGIN
    ALTER TABLE [dbo].[User] ADD [is_activated] BIT NOT NULL CONSTRAINT DF_User_is_activated DEFAULT 0;
END;
