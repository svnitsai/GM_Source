USE [PayC]
GO

/****** Object:  Table [dbo].[DailyPayC]    Script Date: 12/30/2014 19:00:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[DailyPayC1](
	[PayCReferenceNumber] [int] IDENTITY(1,1) NOT NULL,
	[PayCDueDate] [datetime] NOT NULL,
	[CustCode] [numeric](9, 0) NOT NULL,
	[AgentCode] [numeric](9, 0) NULL,
	[InvoiceAmount] [numeric](14, 2) NULL,
	[PayCStatus] [varchar](20) NULL,
	[InvoiceReferenceNumber] [numeric](9, 0) NOT NULL,
	[InvoiceDate] [datetime] NULL,
	[DeferredDate] [datetime] NULL,
	[CreatedDate] [datetime] NOT NULL,
	[CreatedBy] [varchar](50) NOT NULL,
	[UpdatedDate] [datetime] NOT NULL,
	[UpdatedBy] [varchar](50) NOT NULL,
	[OldPayCReferenceNumber] [numeric](9, 0) NOT NULL,
 CONSTRAINT [PK_DailyPayC1] PRIMARY KEY CLUSTERED 
(
	[PayCReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyPayC1] UNIQUE NONCLUSTERED 
(
	[PayCDueDate] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO


INSERT INTO [PayC].[dbo].[DailyPayC1]
           ([PayCDueDate]
           ,[CustCode]
           ,[AgentCode]
           ,[InvoiceAmount]
           ,[PayCStatus]
           ,[InvoiceReferenceNumber]
           ,[InvoiceDate]
           ,[DeferredDate]
           ,[CreatedDate]
           ,[CreatedBy]
           ,[UpdatedDate]
           ,[UpdatedBy]
           ,[OldPayCReferenceNumber])
SELECT [PayCDueDate]
      ,[CustCode]
      ,' '
      ,[InvoiceAmount]
      ,[PayCStatus]
      ,[InvoiceReferenceNumber]
      ,[InvoiceDate]
      ,[DeferredDate]
      ,[CreatedDate]
      ,[CreatedBy]
      ,[UpdatedDate]
      ,[UpdatedBy]
      ,[PayCReferenceNumber]
  FROM [PayC].[dbo].[DailyPayC]
  
 SET ANSI_PADDING OFF
GO 