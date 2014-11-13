USE [Local1]
GO
/****** Object:  Table [dbo].[DailyCollection]    Script Date: 11/07/2014 15:35:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyCollection](
	[CollectionRefereneNumber] 	[int] IDENTITY(1,1) NOT NULL,
	[CollectionDueDate] 		[datetime] NULL,
	[CustCode] 			[numeric](9, 0) NULL,
	[InvoiceAmount] 		[numeric](10, 2) NULL,
	[CollectionStatus] 		[varchar](15) NULL,
	[InvoiceReferenceNumber] 	[numeric](9, 0) NOT NULL,
 CONSTRAINT [PK_DailyCollection] PRIMARY KEY CLUSTERED 
(
	[CollectionRefereneNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyCollection] UNIQUE NONCLUSTERED 
(
	[CollectionDueDate] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF

USE [Local1]
GO
/****** Object:  Table [dbo].[DailyCollectionDetails]    Script Date: 11/07/2014 15:35:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyCollectionDetails](
	[CollectionRefereneNumber] 	[numeric](10,0) NOT NULL,
	[CollectionReferenceSubNumber] 	[numeric](3,0) NOT NULL,
	[CollectionDate] 		[datetime] NULL,
	[CustBankId] 			[numeric](10,0) NULL,
	[CustId] 			[numeric](10,0) NULL,
	[DeferredDate] 			[datetime] NULL,
	[PaidAmount] 			[numeric](10, 2) NULL,
	[AccountLocation] 		[char](15) NULL,
	[LedgerPageNumber] 		[numeric](9, 0) NOT NULL,
 CONSTRAINT [PK_DailyCollectionDetails] PRIMARY KEY CLUSTERED 
(
	[CollectionRefereneNumber] ASC,
        [CollectionReferenceSubNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyCollectionDetails] UNIQUE NONCLUSTERED 
(
	[CollectionDate] ASC,
	[AccountLocation] ASC,
        [LedgerPageNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF


USE [Local1]
GO
/****** Object:  Table [dbo].[DailyPayable]    Script Date: 11/07/2014 16:35:47 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyPayable](
	[PayableReferenceNumber] 	[int] IDENTITY(1,1) NOT NULL,
	[PayableDate] 			[datetime] NULL,
	[PayableAmount] 		[numeric](10, 2) NULL,
	[PaidAmount]			[numeric](10,2) Null,
	[PayableStatus]			[Char] (20) Null,
	[CustId] 			[numeric](9, 0) NOT NULL,
	[CustBankId] 			[numeric](3,0) NOT NULL
 CONSTRAINT [PK_DailyPayable] PRIMARY KEY CLUSTERED 
(
	[PayableReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyPayable] UNIQUE NONCLUSTERED 
(
	[PayableDate] ASC,
	[CustId] ASC,
        [PayableReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF


USE [Local1]
GO
/****** Object:  Table [dbo].[Customer]    Script Date: 11/07/2014 16:10:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Customer](
	[CustId] 		[int] IDENTITY(1,10) NOT NULL,
	[CustCode] 		[numeric](9, 0) NULL,
	[CustType] 		[char](10) NULL,
 	[CustName] 		[varchar] (50) NULL,
	[CustAddress1] 		[varchar] (50) NULL,
	[CustAddress2] 		[varchar] (50) NULL,
	[CustAddress3] 		[varchar] (50) NULL,
	[CustAddress4] 		[varchar] (50) NULL,
	[CustCity] 		[varchar] (50) NULL,
	[CustState]     	[varchar] (50) Null,
	[CustCountry]   	[varchar] (50) null,
	[CustContactNumber] 	[numeric] (16,0) NULL,
	[CustExtension]		[numeric] (7,0) NULL
 CONSTRAINT [PK_Customer] PRIMARY KEY CLUSTERED 
(
	[CustId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
 CONSTRAINT [UQ_DailyPayable] UNIQUE NONCLUSTERED 
(
	[CustId] ASC,
	[CustCode] ASC,
        [CustType] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF

USE [Local1]
GO
/****** Object:  Table [dbo].[CustomerBanks]    Script Date: 11/07/2014 16:10:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CustomerBanks](
	[CustId] 		[numeric](9, 0) NOT NULL,
	[CustBankId] 		[int] IDENTITY(1,1) NOT NULL,
	[CustBank] 		[varchar] (50) NOT NULL,
	[CustBankBranch] 	[varchar] (50) NOT NULL,
	[CustBankAccountType] 	[numeric] (1,0) NOT NULL,
	[CustBankAccountNumber] [varchar] (50) NULL
 CONSTRAINT [PK_CustomerBanks] PRIMARY KEY CLUSTERED 
(
	[CustId] ASC,
	[CustBankId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
)
GO
SET ANSI_PADDING OFF
