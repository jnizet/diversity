import { IdGeneratorService } from './id-generator.service';

describe('IdGeneratorService', () => {
  it('should generate sequential suffixes', () => {
    const service = new IdGeneratorService();
    expect(service.generateSuffix()).toBe('-0');
    expect(service.generateSuffix()).toBe('-1');
    expect(service.generateSuffix()).toBe('-2');
  });
});
