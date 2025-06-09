import { Component, OnInit } from '@angular/core';
import { FnConfigServiceService, TreeNode } from 'src/app/services/fn-config-service.service';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { Router } from '@angular/router';
import { KeycloakService } from 'src/app/services/keycloak.service';
import { MatDialogRef } from '@angular/material/dialog';
import { SecurityService } from 'src/app/services/security.service';
@Component({
  selector: 'app-config-content',
  templateUrl: './config-content.component.html',
  styleUrls: ['./config-content.component.css']
})
export class ConfigContentComponent implements OnInit {

  treeControl = new NestedTreeControl<TreeNode>(node => this.getChildren(node));
  dataSource = new MatTreeNestedDataSource<TreeNode>();
  loading = true;
  selectedNode: TreeNode | null = null;
  isAuthenticated: boolean;
  userName: any;

  constructor(private fnConfig: FnConfigServiceService,
  private security: SecurityService,
    private router: Router,
    private keycloakService: KeycloakService,
    private dialogRef: MatDialogRef<ConfigContentComponent>) { }

  ngOnInit(): void {
 
    // this.openLogin();
    // const userDetailsString = localStorage.getItem('userDetails');
    // const userName = localStorage.getItem('userName')?.replace(/"/g, ''); // Remove double quotes
    // this.userName = userName;
    this.getUserName();
    this.loading = true;
    this.fnConfig.getData().subscribe(data => {
      this.sortNodes(data);
      this.dataSource.data = data;
      this.loading = false;
      const showMethod: string = (localStorage.getItem('selectedSite') as string).toUpperCase();
      const defaultNode = data.find(node => node.name.toUpperCase() === showMethod);
      if (defaultNode) {
        this.selectedNode = defaultNode;
      } else {
        console.warn(`No node found for showMethod: ${showMethod}`);
      }
      // let showMethod: string = localStorage.getItem('selectedSite') as string;
      this.selectNodeByName(showMethod);
      this.treeControl.dataNodes.forEach(node => this.treeControl.collapse(node));
    }, error => {
      this.loading = false;
    });
  }
  // openLogin() {
  //   localStorage.setItem('showmethod', 'true');
  //   const showPop = localStorage.getItem('showPop');
  //   if (showPop == 'true') {
  //   }
  //   this.keycloakService.initializeKeycloak().then(authenticated => {
  //     this.isAuthenticated = authenticated;
  //     localStorage.setItem('showPop', 'false');
  //   }).catch(err => {
  //     console.error('Error during Keycloak initialization:', err);
  //   });
  // }
  hasChild = (_: number, node: TreeNode) => !!this.getChildren(node)?.length;

  getChildren(node: TreeNode): TreeNode[] | undefined {
    return node.children;
  }

  onNodeSelected(node: TreeNode): void {
    this.selectedNode = node;
  }

  selectNodeByName(name: string): void {
    this.fnConfig.getNodeDetailsByName(name).subscribe(node => {
      if (node) {
        this.selectedNode = node;
        this.expandNode(node);
      }
    });
  }

  expandNode(node: TreeNode): void {
    let currentNode: TreeNode | null = node;
    while (currentNode) {
      this.treeControl.expand(currentNode);
      currentNode = this.getParentNode(currentNode);
    }
  }

  getParentNode(node: TreeNode): TreeNode | null {
    return null;
  }

  getNodeLabel(node: TreeNode): string {
    switch (node.type) {
      case 'first':
        return 'Plant -';
      case 'second':
        return 'Group Type -';
      case 'third':
        return 'Dept -';
      case 'fourth':
        return 'Group -';
      case 'fifth':
        return 'PP -';
      default:
        return 'PP -';
    }
  }

  getNodeDisplayName(node: TreeNode): string {
    return node.name || node.galcProcessPointName || node.groupTypeName || '';
  }

  private sortNodes(nodes: TreeNode[]): void {
    nodes.sort((a, b) => {
      return a.sequenceNumber - b.sequenceNumber;
    });

    nodes.forEach(node => {
      if (node.children) {
        this.sortNodes(node.children);
      }
    });
  }

  getNodeStyle(node: TreeNode): string {
    if (node.type === 'second') {
      if (node.groupType === 1) {
        return 'second-node-yellow';
      } else if (node.groupType === 2) {
        return 'second-node-green';
      } else if (node.groupType === 3) {
        return 'second-node-blue';
      } else {
        return 'second-node-orange';
      }
    }
    return '';
  }

  // private loadUserProfile(): void {
  //   this.keycloakService.loadUserProfile().then(profile => {
  //   }).catch(err => {
  //     console.error('Failed to load user profile after token refresh:', err);
  //   });


  // }
  getUserName(): void {
    this.security.getUserProfile();
    const userDetailsString = localStorage.getItem('userDetails');

    if (userDetailsString) {
      try {
        const userDetails = JSON.parse(userDetailsString);
        console.log(userDetails);
        this.userName = userDetails?.username ?? 'visitor';
      } catch (e) {
        console.error('Failed to parse user details from localStorage:', e);
        this.userName = 'visitor';
      }
    } else {
      this.userName = 'visitor';
    }
  }

  // login(): void {
  //   this.keycloakService.login();
  // }
  logout(): void {
    // localStorage.setItem('showPop', 'flase');
    this.security.logout();
    this.dialogRef.close();
  }
}