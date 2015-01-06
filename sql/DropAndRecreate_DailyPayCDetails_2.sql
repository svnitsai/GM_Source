USE [PayC]
GO

/****** Object:  Table [dbo].[DailyPayCDetailsNew]    Script Date: 01/05/2015 19:39:56 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

DROP TABLE PayC.dbo.DailyPayCDetails

CREATE TABLE [dbo].[DailyPayCDetails](
	[PayCReferenceSubNumber] [int] IDENTITY(1,1) NOT NULL,
	[PayCReferenceNumber] [numeric](10, 0) NOT NULL,
	[PayCDate] [datetime] NULL,
	[PayCType] [varchar](10) NULL,
	[SupplierCode] [numeric](9, 0) NULL,
	[SupplierBankId] [numeric](10, 0) NULL,
	[PaidAmount] [numeric](10, 2) NULL,
	[Comments] [varchar](50) NULL,
	[AccountLocationCode] [numeric](9, 0) NULL,
	[LedgerPageNumber] [numeric](9, 0) NULL,
	[CreatedDate] [datetime] NOT NULL,
	[CreatedBy] [varchar](50) NOT NULL,
	[UpdatedDate] [datetime] NULL,
	[UpdatedBy] [varchar](50) NULL,
	[Deleted] [int] NOT NULL,
 CONSTRAINT [PK_DailyPayCDetails] PRIMARY KEY CLUSTERED 
(
	[PayCReferenceNumber] ASC,
	[PayCReferenceSubNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[DailyPayCDetails] ADD  CONSTRAINT [DF_DailyPayCDetails_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO


INSERT INTO [PayC].[dbo].[DailyPayCDetails]
           ([PayCReferenceNumber]
           ,[PayCDate]
           ,[PayCType]
           ,[SupplierCode]
           ,[SupplierBankId]
           ,[PaidAmount]
           ,[AccountLocationCode]
           ,[LedgerPageNumber]
           ,[CreatedDate]
           ,[CreatedBy]
           ,[UpdatedDate]
           ,[UpdatedBy]
           ,[Deleted])
 SELECT [PayCReferenceNumber]
      ,[PayCDate]
      ,[PayCType]
      ,[SupplierCode]
      ,[SupplierBankId]
      ,[PaidAmount]
      ,[AccountLocationCode]
      ,[LedgerPageNumber]
      ,[CreatedDate]
      ,[CreatedBy]
      ,[UpdatedDate]
      ,[UpdatedBy]
      ,[Deleted]
  FROM [PayC].[dbo].[DailyPayCDetails1]
GO

