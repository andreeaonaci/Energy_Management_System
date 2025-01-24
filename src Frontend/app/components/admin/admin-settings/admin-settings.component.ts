import { Component, OnInit } from '@angular/core';
import { SettingsService } from '../../../services/settings.service';

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings.component.html',
  styleUrls: ['./admin-settings.component.scss']
})
export class AdminSettingsComponent implements OnInit {
  settings = {
    siteTitle: '',
    theme: 'light',
    notificationsEnabled: false
  };

  constructor(private settingsService: SettingsService) {}

  ngOnInit(): void {
    this.loadSettings();
  }

  loadSettings(): void {
    this.settingsService.getSettings().subscribe((loadedSettings) => {
      this.settings = loadedSettings;
    });
  }

  saveSettings(): void {
    this.settingsService.saveSettings(this.settings).subscribe(() => {
      alert('Settings updated successfully!');
    });
  }
}