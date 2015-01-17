USE [PayC]
GO

/****** Object:  Table [dbo].[DailyPayC]    Script Date: 12/30/2014 19:00:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO
  
	EXEC sp_rename 'DailyPayC', 'DailyPayC_Old';

USE [PayC]
GO

	EXEC sp_rename 'DailyPayC1', 'DailyPayC';

ALTER TABLE DailyPayC DROP COLUMN OldPayCReferenceNumber


/****** Object:  Index [PK_DailyPayCNew]    Script Date: 12/30/2014 23:22:24 ******/
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[DailyPayC]') AND name = N'PK_DailyPayC1')
ALTER TABLE [dbo].[DailyPayC] DROP CONSTRAINT [PK_DailyPayC1]
ALTER TABLE [dbo].[DailyPayC_Old] DROP CONSTRAINT [PK_DailyPayC]
ALTER TABLE [dbo].[DailyPayC_Old] DROP CONSTRAINT [UQ_DailyPayC]

GO

USE [PayC]
GO

/****** Object:  Index [PK_DailyPayCNew]    Script Date: 12/30/2014 23:22:25 ******/
ALTER TABLE [dbo].[DailyPayC] ADD  CONSTRAINT [PK_DailyPayC] PRIMARY KEY CLUSTERED 
(
	[PayCReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO




USE [PayC]
GO

/****** Object:  Index [UQ_DailyPayCNew]    Script Date: 12/30/2014 23:22:31 ******/
IF  EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[DailyPayC]') AND name = N'UQ_DailyPayC1')
ALTER TABLE [dbo].[DailyPayC] DROP CONSTRAINT [UQ_DailyPayC1]
GO

USE [PayC]
GO

/****** Object:  Index [UQ_DailyPayCNew]    Script Date: 12/30/2014 23:22:32 ******/
ALTER TABLE [dbo].[DailyPayC] ADD  CONSTRAINT [UQ_DailyPayC] UNIQUE NONCLUSTERED 
(
	[PayCDueDate] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO


SET ANSI_PADDING OFF
GO