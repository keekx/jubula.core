/*******************************************************************************
 * Copyright (c) 2004, 2010 BREDEX GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BREDEX GmbH - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.jubula.client.core.persistence;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jubula.client.core.events.DataEventDispatcher;
import org.eclipse.jubula.client.core.events.DataEventDispatcher.TestresultState;
import org.eclipse.jubula.client.core.model.ITestResultPO;
import org.eclipse.jubula.client.core.model.PoMaker;
import org.eclipse.jubula.tools.exception.GDException;
import org.eclipse.jubula.tools.exception.GDFatalException;
import org.eclipse.jubula.tools.exception.GDProjectDeletedException;
import org.eclipse.jubula.tools.messagehandling.MessageIDs;


/**
 * PM to handle all test result related hibernate queries
 * 
 * @author BREDEX GmbH
 * @created Mar 3, 2010
 */
public class TestResultPM {
    
    /**
     * hide
     */
    private TestResultPM() {
    // empty
    }

    /**
     * store test result details of test result node in database
     * @param session Session
     */
    public static final void storeTestResult(EntityManager session) {
        try {            
            final EntityTransaction tx = 
                Hibernator.instance().getTransaction(session);
            Hibernator.instance().commitTransaction(session, tx);
        } catch (PMException e) {
            throw new GDFatalException("storing of test results failed.", e, //$NON-NLS-1$
                    MessageIDs.E_DATABASE_GENERAL);
        } catch (GDProjectDeletedException e) {
            throw new GDFatalException("storing of test results failed.", e, //$NON-NLS-1$
                    MessageIDs.E_PROJECT_NOT_FOUND);
        } finally {
            Hibernator.instance().dropSession(session);
        }
    }
    
    /**
     * delete test result elements of selected summary
     * @param resultId id of test result
     */
    public static final void deleteTestresultOfSummary(
            Long resultId) {
        
        if (Hibernator.instance() == null) {
            return;
        }
        final EntityManager session = Hibernator.instance().openSession();
        try {
            final EntityTransaction tx = 
                Hibernator.instance().getTransaction(session);
            
            executeDeleteTestresultOfSummary(session, resultId);
            
            Hibernator.instance().commitTransaction(session, tx);
        } catch (PMException e) {
            throw new GDFatalException("delete testresult element failed.", e, //$NON-NLS-1$
                    MessageIDs.E_DATABASE_GENERAL);
        } catch (GDProjectDeletedException e) {
            throw new GDFatalException("delete testresult element failed.", e, //$NON-NLS-1$
                    MessageIDs.E_PROJECT_NOT_FOUND);
        } finally {
            Hibernator.instance().dropSession(session);
        }
    }
    
    /**
     * execute delete-test-result of summary without commit
     * @param session Session
     * @param resultId id of testresult-summary-entry, or <code>null</code> if 
     *                 all test results should be deleted.
     */
    @SuppressWarnings("unchecked")
    public static final void executeDeleteTestresultOfSummary(
            EntityManager session, Long resultId) {
        boolean isDeleteAll = resultId == null;
        
        //get list of parameter detail ids to delete
        List<Number> paramIdList = null;
        if (!isDeleteAll) {
            Query paramIdQuery = session.createNativeQuery(
                    "select CHILD from PARAMETER_LIST where PARENT in "  //$NON-NLS-1$
                    + "(select ID from TESTRESULT where INTERNAL_TESTRUN_ID = #resultId)"); //$NON-NLS-1$
            paramIdQuery.setParameter("resultId", resultId); //$NON-NLS-1$
            paramIdList = paramIdQuery.getResultList();
        }

        //delete parameter list of test result
        StringBuilder paramListQueryBuilder = new StringBuilder();
        paramListQueryBuilder.append("delete from PARAMETER_LIST "); //$NON-NLS-1$
        if (!isDeleteAll) {
            paramListQueryBuilder.append(" where PARENT in " + //$NON-NLS-1$
                "(select ID from TESTRESULT where INTERNAL_TESTRUN_ID = #id)"); //$NON-NLS-1$
        }
        Query paramListQuery = 
            session.createNativeQuery(paramListQueryBuilder.toString());
        if (!isDeleteAll) {
            paramListQuery.setParameter("id", resultId); //$NON-NLS-1$
        }
        paramListQuery.executeUpdate();

        //delete test result details
        StringBuilder resultQueryBuilder = new StringBuilder();
        resultQueryBuilder.append(
                "delete from TestResultPO testResult"); //$NON-NLS-1$
        if (!isDeleteAll) {
            resultQueryBuilder.append(" where testResult.internalTestResultSummaryID = :id"); //$NON-NLS-1$
        }
        Query resultQuery = session.createQuery(resultQueryBuilder.toString());
        if (!isDeleteAll) {
            resultQuery.setParameter("id", resultId); //$NON-NLS-1$
        }
        resultQuery.executeUpdate();

        //delete parameter details of test results
        
        String paramQueryBaseString = 
            "delete from ParameterDetailsPO details"; //$NON-NLS-1$
        if (isDeleteAll) {
            session.createQuery(paramQueryBaseString).executeUpdate();
        } else {
            if (paramIdList != null) {
                for (Number paramId : paramIdList) {
                    Query paramQuery = session.createQuery(paramQueryBaseString
                            + " where details.id =:id"); //$NON-NLS-1$
                    paramQuery.setParameter("id", paramId.longValue()); //$NON-NLS-1$
                    paramQuery.executeUpdate();
                }
            }
        }
    }
    
    /**
     * delete all test result details
     */
    public static final void deleteAllTestresultDetails() {
        deleteTestresultOfSummary(null);
    }
    

    /**
     * clean test result details by age (days of existence)
     * testrun summaries will not be deleted
     * @param days days
     * @param projGUID the project guid
     * @param majorVersion the project major version number
     * @param minorVersion the project minor version number
     */
    public static final void cleanTestresultDetails(int days, String projGUID,
        int majorVersion, int minorVersion) {
        Date cleanDate = DateUtils.addDays(new Date(), days * -1);
        try {
            Set<Long> summaries = TestResultSummaryPM
                    .findTestResultSummariesByDate(cleanDate, projGUID,
                            majorVersion, minorVersion);
            for (Long summaryId : summaries) {
                deleteTestresultOfSummary(summaryId);
            }
            DataEventDispatcher.getInstance().fireTestresultChanged(
                    TestresultState.Refresh);
        } catch (GDException e) {
            throw new GDFatalException("deleting testresults failed.", e, //$NON-NLS-1$
                    MessageIDs.E_DELETE_TESTRESULT);
        } 
    }
    
    /**
     * @param session The session in which to execute the Hibernate query.
     * @param summaryId The database ID of the summary for which to compute the
     *                  corresponding Test Result nodes.
     * @return the Test Result nodes associated with the given Test Result 
     *         Summary, sorted by sequence (ascending).
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static List<ITestResultPO> computeTestResultListForSummary(
            EntityManager session, Long summaryId) {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(PoMaker.getTestResultClass());
        query.orderBy(builder.asc(from.get("keywordSequence"))) //$NON-NLS-1$
            .select(from).where(
                builder.equal(from.get("internalTestResultSummaryID"), summaryId)); //$NON-NLS-1$
        
        return session.createQuery(query).getResultList();
    }
    
    /**
     * @param session The session in which to execute the Hibernate query.
     * @param summaryId The database ID of the summary for which to compute the
     *                  corresponding Test Result nodes.
     * @return the Test Result nodes associated with the given Test Result 
     *         Summary, sorted by sequence (ascending).
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean hasTestResultDetails(
            EntityManager session, Long summaryId) {
        boolean hasDetails = false;
        
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Root from = query.from(PoMaker.getTestResultClass());
        query.select(builder.count(from)).where(
            builder.equal(from.get("internalTestResultSummaryID"), summaryId)); //$NON-NLS-1$
        
        Number result = (Number)session.createQuery(query).getSingleResult();
        if (result.longValue() > 0) {
            hasDetails = true;
        }
        return hasDetails;
    }
    
    /**
     * @param session
     *            The session in which to execute the Hibernate query.
     * @return a list of test result ids that have test result details
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static List<Number> 
    computeTestresultIdsWithDetails(EntityManager session) {

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery();
        Path from = query.from(PoMaker.getTestResultClass()).get("internalTestResultSummaryID"); //$NON-NLS-1$
        query.select(from).distinct(true);
        
        return session.createQuery(query).getResultList();
    }
}