import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { getRandomColor } from 'src/app/utils';

@Component({
  selector: 'comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.css'],
})
export class Comments implements OnInit {
  @Input() selectedOptions: string[] = [];
  @Input() options: string[] = [];
  @Input() commentsStr = '';
  @Output() onAddComments = new EventEmitter<string>();

  comments: string[] = [];

  ngOnInit(): void {
    if (this.commentsStr?.length) {
      const commentsArr = this.commentsStr.split('; ');

      this.selectedOptions = [];

      commentsArr.forEach((comment) => {
        const optionsArrJoined = this.options.map((option) =>
            option.split(' ').join('').toLowerCase()
          );

        const indexOfCommentFound = optionsArrJoined.indexOf(comment.split(' ').join('').toLowerCase());

        if (indexOfCommentFound !== -1) {
          this.selectedOptions.push(this.options[indexOfCommentFound]);
        }
      });
    }
  }

  onOptionsChange(event: MatSelectChange) {
    this.selectedOptions = event.value;
    this.updateComments();
  }

  updateComments() {
    let commentsStr = '';
    if (this.selectedOptions.length > 0) {
      commentsStr += this.selectedOptions.join('; ');
    }
    this.commentsStr = commentsStr;
    this.onAddComments.emit(commentsStr);
  }

  getBgColor(str: string): string {
    return getRandomColor(str);
  }
}
