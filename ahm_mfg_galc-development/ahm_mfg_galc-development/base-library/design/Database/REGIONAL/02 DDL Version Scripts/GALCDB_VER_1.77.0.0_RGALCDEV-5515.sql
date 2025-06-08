update galadm.part_spec_tbx set PART_SERIAL_NUMBER_MASK = CHAR(REPLACE(PART_SERIAL_NUMBER_MASK,'*','<<*>>'));
update galadm.part_spec_tbx set PART_SERIAL_NUMBER_MASK = CHAR(REPLACE(PART_SERIAL_NUMBER_MASK,'#','<<#>>'));
update galadm.part_spec_tbx set PART_SERIAL_NUMBER_MASK = CHAR(REPLACE(PART_SERIAL_NUMBER_MASK,'?','<<?>>'));
update galadm.part_spec_tbx set PART_SERIAL_NUMBER_MASK = CHAR(REPLACE(PART_SERIAL_NUMBER_MASK,'^','<<^>>'));
update galadm.part_spec_tbx set PART_SERIAL_NUMBER_MASK = CHAR(REPLACE(PART_SERIAL_NUMBER_MASK,'%','<<%>>'));