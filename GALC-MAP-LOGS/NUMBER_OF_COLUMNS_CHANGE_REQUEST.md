# Change Request: Increase NUMBER_OF_COLUMNS Property for MAP Team Leader Run Sheets

## Change Request Information

- **CR Number**: [To be assigned]
- **CR Title**: Increase NUMBER_OF_COLUMNS Property for MAP Team Leader Run Sheets
- **Requester**: Mike
- **Date Requested**: [Current Date]
- **Requested Implementation Date**: [End of Next Week]
- **Priority**: Medium
- **Change Type**: Configuration

## Business Justification

The Business Objects report for MAP Team Leader Run sheets has been updated to support up to 25 columns (previously limited to 20). The GALC properties need to be updated to match this new limit. This change will allow the MAP team to configure additional parts for teams that need it, improving the efficiency of the manufacturing process.

## Scope of Change

Update the NUMBER_OF_COLUMNS property from 20 to 25 for all line 1 teams (approximately 50 teams) in the GALC system.

## Technical Details

- **Component**: GALC Configuration Properties
- **Database Table**: GAL489TBX
- **Property Key**: NUMBER_OF_COLUMNS
- **Current Value**: 20
- **New Value**: 25
- **Affected Teams**: All line 1 teams (component IDs starting with "1-")

## Implementation Plan

### Phase 1: Test with a Small Subset of Teams
1. Identify 2-3 low-impact teams with Matt
2. Update the NUMBER_OF_COLUMNS property for these teams
3. Verify reports generate correctly with the new setting

### Phase 2: Roll Out to Remaining Teams
1. Once validated, update the remaining line 1 teams
2. Skip any teams Matt identifies as no longer needed
3. Verify all reports display correctly

## Implementation Method

The change will be implemented using one of the following methods:

1. **SQL Script**: Direct database update using the provided SQL script
2. **Java Program**: Programmatic update using the GALC API

Both methods include backup, verification, and rollback capabilities.

## Testing Plan

A comprehensive test plan has been created (see NUMBER_OF_COLUMNS_TEST_PLAN.md) that includes:

1. Property update verification
2. Functional testing of report generation
3. Verification of column display
4. Regression testing
5. Performance testing

## Risk Assessment

### Potential Risks

1. **Report Generation Failure**: 
   - Risk Level: Low
   - Mitigation: Phased implementation with testing after each phase

2. **Incorrect Column Display**: 
   - Risk Level: Low
   - Mitigation: Verify reports display only the configured number of columns

3. **Impact on Other Functions**: 
   - Risk Level: Very Low
   - Mitigation: Regression testing of other Team Leader functions

### Rollback Plan

If issues are encountered:

1. Execute the rollback script or function to restore the original property values
2. Verify the properties are restored correctly
3. Generate reports to confirm they work with the original property values

## Resources Required

- Database Administrator (for SQL script execution)
- GALC Developer (for Java program execution)
- QA Tester (for verification)
- Business User (Matt) for validation

## Documentation Updates

No documentation updates are required as this is a configuration change that does not affect user interfaces or procedures.

## Approvals

- **Requester**: ________________________ Date: ________
- **Technical Lead**: ___________________ Date: ________
- **QA Lead**: _________________________ Date: ________
- **Change Manager**: __________________ Date: ________
- **Business Owner**: __________________ Date: ________

## Implementation Results

- **Implemented By**: __________________ Date: ________
- **Implementation Status**: [Success/Failure]
- **Notes**: [Any notes about the implementation]

## Post-Implementation Review

- **Reviewer**: ________________________ Date: ________
- **Status**: [Successful/Issues Found]
- **Notes**: [Any notes about the review]