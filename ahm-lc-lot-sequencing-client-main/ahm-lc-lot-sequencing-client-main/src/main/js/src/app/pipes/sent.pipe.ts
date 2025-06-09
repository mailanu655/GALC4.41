import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sentPipe',
})
export class SentPipe implements PipeTransform {
  transform(id: number): string {
    let renderedData = 'Unkown';
    switch (id) {
      case 0:
        renderedData = 'WAITING';
        break;
      case 1:
        renderedData = 'IN PROGRESS';
        break;
      case 2:
        renderedData = 'DONE';
        break;
      case 3:
        renderedData = 'SENT';
        break;
      default:
        break;
    }

    return renderedData;
  }
}
