USE [PayC]
GO

/****** Object:  StoredProcedure [dbo].[CS_CSALESEXTRACT]    Script Date: 12/30/2014 23:00:54 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

	SET NOCOUNT ON
	SET XACT_ABORT ON 
GO
/*********** check for TODO items **********/
/* 
	Irrespective of the extraction done or not for a day, this Stored Procedure will 
	extract data once it is invoked. Every day extraction happens. Once update or insert of 
	extracted data need to update that credit sales extration has been done

*/
	DECLARE @UPDATEDDT			DATETIME
	DECLARE @InvoiceDate		DATETIME

	DECLARE @InvoiceRefNum1		NUMERIC(9,0)
	DECLARE @InvoiceRefNum2		NUMERIC(9,0)
/* 
	checking for initial date which is used as from date for extracton of invoice. 
	there should be only 1 row 
*/
/*
	Insert a row for 'Credit Sales' data extraction - flag 'C' stands for 'Credit Sales'
	with today's date

	To extract credit sales details from database local1 with a time range
*/
	DECLARE CUR_DailyPayCNew CURSOR
	LOCAL FAST_FORWARD  FOR
		SELECT InvoiceReferenceNumber, InvoiceDate
		From DailyPayCNew
		
/*
	TODO - Add TRNTYPE condition in where clause
*/
	OPEN CUR_DailyPayCNew
	FETCH CUR_DailyPayCNew INTO @InvoiceRefNum1, @InvoiceDate
	
	SET @UPDATEDDT = GETDATE()
	BEGIN TRANSACTION DP_TRAN

	WHILE @@FETCH_STATUS = 0
	BEGIN  
		/*
			Check for existing row and allow only changes to 'Net Invoice Amount' and 'Due Days'
		*/
		SET @UPDATEDDT = GETDATE()
		
		SELECT @UPDATEDDT = PayCDueDate 
	    FROM test1payc..DailyPayC
		WHERE [InvoiceReferenceNumber] = @InvoiceRefNum1 
		/*
			Check for existing row
		*/
		UPDATE dailypaycnew
			SET [invoiceDate]   = @UPDATEDDT
		WHERE [InvoiceReferenceNumber] = @InvoiceRefNum1

		FETCH CUR_DailyPayCNew INTO @InvoiceRefNum1, @InvoiceDate

	END
    CLOSE CUR_DailyPayCNew


	COMMIT TRANSACTION DP_TRAN

	SELECT @@ERROR AS ReturnValue


GO
